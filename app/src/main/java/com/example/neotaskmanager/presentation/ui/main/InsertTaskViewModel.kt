package com.example.neotaskmanager.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class InsertTaskViewModel(private val repository: TaskRepository) : ViewModel() {
    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
        }
    }
}