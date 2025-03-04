package com.guilhermekunz.gerenciadordetarefas.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkerParameters
import com.guilhermekunz.gerenciadordetarefas.data.database.TaskDatabase
import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.data.network.TaskApiService
import com.guilhermekunz.gerenciadordetarefas.data.repository.TaskRepositoryImpl
import com.guilhermekunz.gerenciadordetarefas.data.worker.TaskSyncWorker
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetAllTasksUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetTaskByIdUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.RoomCreateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateCheckBoxTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.addtask.AddTaskViewModel
import com.guilhermekunz.gerenciadordetarefas.presentation.edittask.EditTaskViewModel
import com.guilhermekunz.gerenciadordetarefas.presentation.listtask.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { AddTaskViewModel(get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { EditTaskViewModel(get(), get()) }
}

val repositoryModule = module {
    factory <Repository> {
        TaskRepositoryImpl(
            get(), get()
        )
    }
}

val useCaseModule = module {
    factory { GetAllTasksUseCase(get()) }
    factory { RoomCreateTaskUseCase(get()) }
    factory { UpdateCheckBoxTaskUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
}

val workerModule = module {
    factory { (context: Context, params: WorkerParameters) ->
        TaskSyncWorker(context, params)
    }
}

val networkModule = module {
    single { provideRetrofit() }
    single { get<Retrofit>().create(TaskApiService::class.java) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val userDateBuilder = module {
    fun provideDatabase(application: Application): TaskDatabase {
        return Room.databaseBuilder(
            application, TaskDatabase::class.java, "task_db"
        )
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration()
            .build()


    }

    fun provideDao(database: TaskDatabase): TaskDao {
        return database.taskDao
    }

    single { provideDatabase(application = androidApplication()) }
    single { provideDao(database = get()) }
}