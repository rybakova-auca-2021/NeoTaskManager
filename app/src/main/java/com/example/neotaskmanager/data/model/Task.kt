package com.example.neotaskmanager.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Parcelize
@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String? = null,
    val isSaved: Boolean? = null,
    val subTasks: @RawValue List<TaskData>? = null
) : Parcelable

data class TaskData(
    val title: String,
    val completed: Boolean
)

class Converters {
    @TypeConverter
    fun fromJson(value: String): List<TaskData> {
        val listType = object : TypeToken<List<TaskData>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<TaskData>): String {
        return Gson().toJson(list)
    }
}