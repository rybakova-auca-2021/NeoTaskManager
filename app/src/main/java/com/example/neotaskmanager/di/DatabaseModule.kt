package com.example.neotaskmanager.di

import com.example.neotaskmanager.data.repository.TaskRepository
import com.example.neotaskmanager.data.repository.TaskRepositoryImpl
import com.example.neotaskmanager.data.source.TaskDatabase
import com.example.neotaskmanager.presentation.ui.main.DeleteTaskViewModel
import com.example.neotaskmanager.presentation.ui.main.GetCategoriesViewModel
import com.example.neotaskmanager.presentation.ui.main.GetTasksViewModel
import com.example.neotaskmanager.presentation.ui.main.InsertTaskViewModel
import org.koin.android.ext.koin.androidContext
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
    single { TaskDatabase.getInstance(androidContext()) }
    single { get<TaskDatabase>().taskDao() }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { GetCategoriesViewModel(get()) }
    viewModel { GetTasksViewModel(get()) }
    viewModel { InsertTaskViewModel(get()) }
    viewModel { DeleteTaskViewModel(get()) }
}