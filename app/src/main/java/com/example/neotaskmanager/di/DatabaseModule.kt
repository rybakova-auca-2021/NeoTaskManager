package com.example.neotaskmanager.di

import androidx.room.Room
import com.example.neotaskmanager.data.repository.TaskRepository
import com.example.neotaskmanager.data.repository.TaskRepositoryImpl
import com.example.neotaskmanager.data.source.TaskDao
import com.example.neotaskmanager.data.source.TaskDatabase
import com.example.neotaskmanager.presentation.ui.main.MainPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            databaseModule,
            repositoryModule,
            viewModelModule)
    )
}

val databaseModule = module {
    single { Room.databaseBuilder(get(),TaskDatabase::class.java, "task_database").build() }
    single { get<TaskDatabase>().taskDao() }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { MainPageViewModel(get()) }
}