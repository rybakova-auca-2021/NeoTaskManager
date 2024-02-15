package com.example.neotaskmanager.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neotaskmanager.data.model.Converters
import com.example.neotaskmanager.data.model.Task


@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao?

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase? {
            if (INSTANCE == null) {
                synchronized(TaskDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            TaskDatabase::class.java, "task_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}
