package com.example.neotaskmanager.presentation.ui.main

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
        (requireActivity() as MainActivity).showBtmNav()
        mPicker = binding.dayScrollDatePicker
        recyclerView = binding.rvTasks
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setCurrentMonth()
        setupNavigation()
        getValue()
        observeTasks()
        setupPopUpMenu()
        setupAdapterClicks()
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

    private fun setCurrentMonth() {
        val dateFormat = SimpleDateFormat("MMMM", Locale("ru"))
        val currentDate = Date()
        val currentMonth = dateFormat.format(currentDate)
        binding.btnCalendar.text = currentMonth
    }

    private fun observeTasks() {
        taskViewModel.fetchTasks()
        taskViewModel.result.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { tasks ->
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

    private fun setupPopUpMenu() {
        lifecycleScope.launch {
            viewModel.fetchCategories()
            viewModel.result.observe(viewLifecycleOwner) { result ->
                result.onSuccess { categories ->
                    if (categories.isNotEmpty()) {
                        showCategoryMenu(categories)
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

    private fun showCategoryMenu(categories: List<String>) {
        val spinner = binding.spinner
        val categoriesWithAll = mutableListOf("Все").apply { addAll(categories) }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesWithAll)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoriesWithAll[position]
                // Handle category selection
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }
    }

    private fun getValue() {
        mPicker.getSelectedDate { date ->
            date?.let {
                val selectedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
            }
        }
    }

    fun addEmptyCard() {
        val emptyTask = Task()
        adapter.items.add(emptyTask)
        adapter.notifyItemInserted(adapter.items.size - 1)
        binding.textNoTasks.visibility = View.GONE
    }

    private fun setupAdapterClicks() {
        adapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onTaskItemClick(item: Task?) {
                println("clicked")
            }

            override fun onSpinnerClickListener() {
                println("on spinner clicked")
            }

            override fun onDeleteClick(item: Task?) {
                lifecycleScope.launch {
                    item?.let {
                        deleteTaskViewModel.deleteTask(it.id)
                    }
                }
                Snackbar.make(binding.root, "Заметка отправлена в корзину", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                    .setAction("Отменить") {
                        lifecycleScope.launch {
                            if (item != null) {
                                insertTaskViewModel.insertTask(item)
                            }
                            adapter.items.add(item)
                            adapter.notifyItemInserted(adapter.items.size - 1)
                        }
                    }
                .show()
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}