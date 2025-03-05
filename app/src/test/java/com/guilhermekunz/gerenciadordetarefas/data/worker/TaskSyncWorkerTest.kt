package com.guilhermekunz.gerenciadordetarefas.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
class TaskSyncWorkerTest {

    private lateinit var worker: TaskSyncWorker
    private lateinit var context: Context
    private lateinit var workerParams: WorkerParameters
    private val repository: Repository = mockk(relaxed = true)

    @Before
    fun setup() {
        stopKoin() // Para qualquer instância anterior do Koin
        context = mockk(relaxed = true)
        workerParams = mockk(relaxed = true)

        startKoin {
            modules(
                module {
                    single { repository }
                }
            )
        }

        worker = TaskSyncWorker(context, workerParams)
    }

    @Test
    fun `doWork should return success when syncTasksWithServer succeeds`() = runTest {
        coEvery { repository.syncTasksWithServer() } returns Unit

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify { repository.syncTasksWithServer() }
    }

    @Test
    fun `doWork should return retry when syncTasksWithServer throws exception`() = runTest {
        coEvery { repository.syncTasksWithServer() } throws Exception("Network Error")

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
        coVerify { repository.syncTasksWithServer() }
    }
}