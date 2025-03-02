package com.guilhermekunz.gerenciadordetarefas.data.di

import android.app.Application
import androidx.room.Room
import com.guilhermekunz.gerenciadordetarefas.data.database.TaskDatabase
import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.data.repository.TaskRepositoryImpl
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.DeleteTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetAllTasksUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.SaveTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.addtask.AddTaskViewModel
import com.guilhermekunz.gerenciadordetarefas.presentation.listtask.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AddTaskViewModel(get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
}

val repositoryModule = module {
    factory <Repository> {
        TaskRepositoryImpl(
            get()
        )
    }
}

val useCaseModule = module {
    factory { GetAllTasksUseCase(get()) }
    factory { SaveTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
}

val userDateBuilder = module {
    fun provideDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(
            application, TaskDatabase::class.java, "task_db"
        )
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    }

    fun provideDao(database: TaskDatabase): TaskDao {
        return database.taskDao
    }

    single { provideDatabase(application = androidApplication()) }
    single { provideDao(database = get()) }
}