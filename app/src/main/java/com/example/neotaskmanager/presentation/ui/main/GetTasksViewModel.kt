package com.example.neotaskmanager.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetTasksViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _result = MutableLiveData<Result<MutableList<Task?>?>>()
    val result: LiveData<Result<MutableList<Task?>?>>
        get() = _result


    fun fetchTasks(currentDate: String) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val tasks = repository.allTasks(currentDate)
                _result.value = Result.success(tasks)
            } catch (e: Exception) {
                _result.value = Result.failure(e)
            }
        }
    }
}