package com.example.neotaskmanager.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.neotaskmanager.data.model.Task


@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task?)

    @Update
    fun update(task: Task?)

    @Delete
    fun delete(task: Task?)

    @Query("SELECT * FROM tasks")
    suspend fun allTasks(): MutableList<Task?>?

    @Query("SELECT DISTINCT category FROM tasks")
    suspend fun getCategories(): List<String>
}
