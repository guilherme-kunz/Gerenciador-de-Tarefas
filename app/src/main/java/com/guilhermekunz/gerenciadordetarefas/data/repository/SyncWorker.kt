//package com.guilhermekunz.gerenciadordetarefas.data.repository
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//
//class SyncWorker(
//    context: Context,
//    workerParams: WorkerParameters,
//    private val repository: TaskRepositoryImpl
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result {
//        repository.scheduleSyncWorker()
//        return Result.success()
//    }
//}