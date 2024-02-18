package com.example.neotaskmanager.presentation.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class GetDeletedTaskViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _result = MutableLiveData<Result<MutableList<Task?>?>>()
    val result: LiveData<Result<MutableList<Task?>?>>
        get() = _result

    fun fetchTasks() {
        viewModelScope.launch {
            try {
                val tasks = repository.deletedTasks()
                _result.value = Result.success(tasks)
            } catch (e: Exception) {
                _result.value = Result.failure(e)
            }
        }
    }
}