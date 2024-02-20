package com.example.neotaskmanager.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String? = null,
    val categoryColor: Int? = null,
    val subTasks: @RawValue MutableList<TaskData?>? = null,
    var date: String? = null,
    var isDeleted: Boolean = false
) : Parcelable

@Parcelize
@Entity(tableName = "task_data")
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    var completed: Boolean?,
) : Parcelable
class Converters {
    @TypeConverter
    fun fromJson(value: String): MutableList<TaskData?> {
        val listType = object : TypeToken<MutableList<TaskData?>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: MutableList<TaskData?>): String {
        return Gson().toJson(list)
    }
}