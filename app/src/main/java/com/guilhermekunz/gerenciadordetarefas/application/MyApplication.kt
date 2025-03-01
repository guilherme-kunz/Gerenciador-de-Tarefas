package com.guilhermekunz.gerenciadordetarefas.application

import android.app.Application
import com.guilhermekunz.gerenciadordetarefas.data.di.repositoryModule
import com.guilhermekunz.gerenciadordetarefas.data.di.useCaseModule
import com.guilhermekunz.gerenciadordetarefas.data.di.userDateBuilder
import com.guilhermekunz.gerenciadordetarefas.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    useCaseModule,
                    userDateBuilder
                )
            )
        }
    }
}