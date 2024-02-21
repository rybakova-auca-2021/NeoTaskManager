package com.example.neotaskmanager.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neotaskmanager.data.model.CategoryWithColor
import com.example.neotaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class GetCategoriesViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _result = MutableLiveData<Result<List<CategoryWithColor>>>()
    val result: LiveData<Result<List<CategoryWithColor>>>
        get() = _result

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
                _result.value = Result.success(categories)
            } catch (e: Exception) {
                _result.value = Result.failure(e)
            }
        }
    }
}

