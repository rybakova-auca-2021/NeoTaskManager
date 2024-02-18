package com.example.neotaskmanager.data.repository

import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.source.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun insert(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    override suspend fun update(task: Task) {
        taskDao.update(task)
    }

    override suspend fun delete(taskId: Long) {
        withContext(Dispatchers.IO) {
            taskDao.delete(taskId)
        }
    }

    override suspend fun getCategories(): List<String> {
        return taskDao.getCategories()
    }

    override suspend fun allTasks(): MutableList<Task?>? {
        return taskDao.allTasks()
    }
}