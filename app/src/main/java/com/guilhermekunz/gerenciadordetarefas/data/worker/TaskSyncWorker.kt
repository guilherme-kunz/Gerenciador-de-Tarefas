package com.guilhermekunz.gerenciadordetarefas.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class TaskSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val repository: Repository by inject<Repository>()

    override suspend fun doWork(): Result {
        return try {
            repository.syncTasksWithServer()
            Result.success()
        } catch (e: Exception) {
            Result.retry() // Tenta novamente depois
        }
    }
}

fun scheduleTaskSync(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<TaskSyncWorker>(15, TimeUnit.MINUTES)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "TaskSyncWork",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}