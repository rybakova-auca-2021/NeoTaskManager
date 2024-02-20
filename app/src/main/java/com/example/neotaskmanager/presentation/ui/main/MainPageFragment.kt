package com.example.neotaskmanager.presentation.ui.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.R
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.databinding.FragmentMainPageBinding
import com.example.neotaskmanager.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainPageFragment : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding: FragmentMainPageBinding get() = _binding!!
    private val viewModel: GetCategoriesViewModel by viewModel()
    private val taskViewModel: GetTasksViewModel by viewModel()
    private val insertTaskViewModel: InsertTaskViewModel by viewModel()
    private val deleteTaskViewModel: DeleteTaskViewModel by viewModel()
    private lateinit var mPicker: DayScrollDatePicker
    private lateinit var adapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        (requireActivity() as MainActivity).showBtmNav()
        mPicker = binding.dayScrollDatePicker
        recyclerView = binding.rvTasks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = arguments?.getString("selectedDate")
        setupRecyclerView()
        setCurrentMonth()
        setupNavigation()
        if (date != null) {
            getTasks(date)
            setupPopUpMenu(date)
        }
        getValue()
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter(mutableListOf(), insertTaskViewModel, deleteTaskViewModel, viewLifecycleOwner.lifecycleScope)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
    }

    private fun setupNavigation() {
        binding.btnCalendar.setOnClickListener {
            findNavController().navigate(R.id.calendarFragment)
        }
        val addButton = requireActivity().findViewById<View>(R.id.add_button)
        addButton.setOnClickListener {
            addEmptyCard()
        }
    }

    private fun getTasks(date: String) {
        observeTasks(date)
        setupAdapterClicks(date)
    }

    private fun setCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM", Locale("ru"))
        val currentDate = Date()
        val currentMonth = dateFormat.format(currentDate)
        binding.btnCalendar.text = currentMonth
    }

    private fun observeTasks(currentDate: String) {
        adapter.deleteAllItems()
        taskViewModel.fetchTasks(currentDate)
        taskViewModel.result.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { tasks ->
                println("tasks by date: $tasks")
                if (tasks != null) {
                    if (tasks.isEmpty()) {
                        binding.textNoTasks.visibility = View.VISIBLE
                    } else {
                        binding.textNoTasks.visibility = View.GONE
                        adapter.updateData(tasks)
                    }
                }
            }
        })
    }
    private fun setupPopUpMenu(currentDate: String) {
        lifecycleScope.launch {
            viewModel.fetchCategories()
            viewModel.result.observe(viewLifecycleOwner) { result ->
                result.onSuccess { categories ->
                    if (categories.isNotEmpty()) {
                        showCategoryMenu(categories, currentDate)
                    } else {
                        Snackbar.make(binding.root, "Категорий для фильтра нет", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                            .setAction("Создать") {
                                addEmptyCard()
                            }
                            .show()
                    }
                }.onFailure { error ->
                    Log.e(TAG, "Error fetching categories: $error")
                }
            }
        }
    }

    private fun showCategoryMenu(categories: List<String>, currentDate: String) {
        val spinner = binding.spinner
        val categoriesWithAll = mutableListOf("Все").apply { addAll(categories) }

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesWithAll)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoriesWithAll[position]
                if (selectedCategory == "Все") {
                    observeTasks(currentDate)
                } else {
                    adapter.filterByCategoryAndDate(selectedCategory, currentDate)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                observeTasks(currentDate)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getValue() {
        mPicker.getSelectedDate { date ->
            date?.let {
                val selectedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
                observeTasks(selectedDate)
                setupAdapterClicks(selectedDate)
                setupPopUpMenu(selectedDate)
            }
        }
    }
    private fun addEmptyCard() {
        val emptyTask = Task()
        adapter.items.add(emptyTask)
        adapter.notifyItemInserted(adapter.items.size - 1)
        binding.textNoTasks.visibility = View.GONE
    }

    private fun setupAdapterClicks(selectedDate: String) {
        adapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onDeleteClick(item: Task?) {
                lifecycleScope.launch {
                    item?.let {
                        deleteTaskViewModel.deleteTask(it.id)
                    }
                }
                Snackbar.make(binding.root, "Заметка отправлена в корзину", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                    .setAction("Отменить") {
                        lifecycleScope.launch {
                            if (item != null) {
                                deleteTaskViewModel.restoreTask(item.id)
                            }
                            adapter.items.add(item)
                            adapter.notifyItemInserted(adapter.items.size - 1)
                        }
                    }
                .show()
            }

            override fun onSaveClick(item: Task?) {
                lifecycleScope.launch {
                    if (item != null) {
                        item.date = selectedDate
                        insertTaskViewModel.insertTask(item)
                    }
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}