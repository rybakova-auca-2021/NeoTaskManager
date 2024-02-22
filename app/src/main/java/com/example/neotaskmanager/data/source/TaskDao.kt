package com.example.neotaskmanager.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.neotaskmanager.data.model.CategoryWithColor
import com.example.neotaskmanager.data.model.Task


@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task?)

    @Update
    fun update(task: Task?)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long?): Task?
    @Query("SELECT * FROM tasks WHERE isDeleted = 1")
    suspend fun deletedTasks(): MutableList<Task?>?

    @Query("DELETE FROM tasks WHERE id = :taskId")
    fun delete(taskId: Long)

    @Query("SELECT * FROM tasks WHERE isDeleted = 0 and date == :currentDate")
    suspend fun allTasks(currentDate: String): MutableList<Task?>?

    @Query("SELECT DISTINCT category, categoryColor FROM tasks")
    suspend fun getCategories(): List<CategoryWithColor>

}
