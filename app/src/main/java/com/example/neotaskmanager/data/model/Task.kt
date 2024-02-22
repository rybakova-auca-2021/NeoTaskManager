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
import java.util.UUID


@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var category: String? = null,
    var categoryColor: Int? = null,
    var subTasks: @RawValue MutableList<TaskData?>? = null,
    var date: String? = null,
    var isDeleted: Boolean = false
) : Parcelable

@Parcelize
@Entity(tableName = "task_data")
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String?,
    var completed: Boolean?,
) : Parcelable

data class CategoryWithColor(
    val category: String,
    val categoryColor: Int
)


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