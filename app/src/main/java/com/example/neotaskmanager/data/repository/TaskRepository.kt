package com.example.neotaskmanager.data.repository

import com.example.neotaskmanager.data.model.Task

interface TaskRepository {
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
    suspend fun getCategories(): List<String>
    suspend fun allTasks(): List<Task?>?
}