package com.example.neotaskmanager.data.repository

import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.source.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun insert(task: Task) {
        return withContext(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    override suspend fun update(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    override suspend fun delete(taskId: Long) {
        withContext(Dispatchers.IO) {
            val task = taskDao.getTaskById(taskId)
            task?.let {
                it.isDeleted = true
                taskDao.update(it)
            }
        }
    }

    override suspend fun restoreTask(taskId: Long) {
        withContext(Dispatchers.IO) {
            val task = taskDao.getTaskById(taskId)
            task?.let {
                it.isDeleted = false
                taskDao.update(it)
            }
        }
    }

    override suspend fun deletedTasks(): MutableList<Task?>? {
        return taskDao.deletedTasks()
    }

    override suspend fun getCategories(): List<String> {
        return taskDao.getCategories()
    }

    override suspend fun allTasks(currentDate: String): MutableList<Task?>? {
        return taskDao.allTasks(currentDate)
    }
}