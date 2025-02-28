package com.guilhermekunz.gerenciadordetarefas

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao, private val apiService: TaskApiService, private val workManager: WorkManager) {

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
        scheduleSyncWorker()
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        scheduleSyncWorker()
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
        scheduleSyncWorker()
    }

    fun scheduleSyncWorker() {
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Só sincroniza quando tiver internet
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            "syncWorker",
            ExistingWorkPolicy.REPLACE, // Substitui se já houver um agendamento
            workRequest
        )
    }
}