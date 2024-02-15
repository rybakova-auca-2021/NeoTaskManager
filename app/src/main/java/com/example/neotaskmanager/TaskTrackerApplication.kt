package com.example.neotaskmanager

import android.app.Application
import com.andexert.calendarlistview.library.BuildConfig
import com.example.neotaskmanager.di.injectFeature
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class TaskTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@TaskTrackerApplication)
            injectFeature()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}