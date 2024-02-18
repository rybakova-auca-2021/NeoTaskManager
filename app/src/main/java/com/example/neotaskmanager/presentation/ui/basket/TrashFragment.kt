package com.example.neotaskmanager.presentation.ui.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neotaskmanager.R
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.databinding.FragmentTrashBinding
import com.example.neotaskmanager.presentation.MainActivity
import com.example.neotaskmanager.presentation.ui.main.CategoryAdapter
import com.example.neotaskmanager.presentation.ui.main.DeleteTaskViewModel
import com.example.neotaskmanager.presentation.ui.main.InsertTaskViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrashFragment : Fragment() {
    private var _binding: FragmentTrashBinding? = null
    private val binding: FragmentTrashBinding get() = _binding!!
    private val viewModel: GetDeletedTaskViewModel by viewModel()
    private val deleteTaskViewModel: DeleteTaskViewModel by viewModel()
    private lateinit var adapter: DeletedTasksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrashBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).hideBtmNav()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupNavigation()
        observeTasks()
        setupClickListener()
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.mainPageFragment)
        }
    }

    private fun setupClickListener() {
        adapter.setOnItemClickListener(object: DeletedTasksAdapter.OnItemClickListener{
            override fun onRecoverClick(task: Task) {
                deleteTaskViewModel.restoreTask(task.id)
                adapter.items.remove(task)
                val position = adapter.items.indexOf(task)
                adapter.notifyItemRemoved(position)
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = DeletedTasksAdapter(mutableListOf(), deleteTaskViewModel, viewLifecycleOwner.lifecycleScope)
        binding.rvDeletedTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDeletedTasks.adapter = adapter
    }

    private fun observeTasks() {
        viewModel.fetchTasks()
        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { tasks ->
                if (tasks != null) {
                    adapter.updateData(tasks)
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}