package com.example.neotaskmanager.data.repository

import com.example.neotaskmanager.data.model.Task

interface TaskRepository {
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(taskId: Long)
    suspend fun restoreTask(taskId: Long)
    suspend fun deletedTasks(): MutableList<Task?>?
    suspend fun getCategories(): List<String>
    suspend fun allTasks(): MutableList<Task?>?
}