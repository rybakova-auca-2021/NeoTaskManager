package com.example.neotaskmanager.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neotaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class DeleteTaskViewModel(private val repository: TaskRepository) : ViewModel() {
    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.delete(taskId)
        }
    }
}