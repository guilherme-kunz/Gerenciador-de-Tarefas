package com.guilhermekunz.gerenciadordetarefas.data.repository

import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.data.network.TaskApiService
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class TaskRepositoryImplTest {

    @MockK
    private lateinit var taskDao: TaskDao

    @MockK
    private lateinit var apiService: TaskApiService

    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = TaskRepositoryImpl(taskDao, apiService)
    }

    @Test
    fun `insertTask should insert task with isSynced false`() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        coEvery { taskDao.insertTask(task.copy(isSynced = false)) } just runs

        repository.insertTask(task)

        coVerify { taskDao.insertTask(task.copy(isSynced = false)) }
    }

    @Test
    fun `getAllTasks should return flow from dao`(): Unit = runBlocking {
        val tasks = listOf(
            TaskEntity(1, "Title 1", "Description 1", false, false),
            TaskEntity(2, "Title 2", "Description 2", true, false)
        )
        every { taskDao.getAllTasks() } returns flowOf(tasks)

        val result = repository.getAllTasks().toList()

        assertEquals(tasks, result)
        every { taskDao.getAllTasks() }
    }

    @Test
    fun `deleteTask should call dao deleteTask`() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        coEvery { taskDao.deleteTask(task) } just runs

        repository.deleteTask(task)

        coVerify { taskDao.deleteTask(task) }
    }

    @Test
    fun `update should update task with isSynced false`() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        coEvery { taskDao.update(task.copy(isSynced = false)) } just runs

        repository.update(task)

        coVerify { taskDao.update(task.copy(isSynced = false)) }
    }

    @Test
    fun `getTaskById should return task from dao`() = runBlocking {
        val task = TaskEntity(1, "Title", "Description", false, false)
        coEvery { taskDao.getTaskById(1) } returns task

        val result = repository.getTaskById(1)

        assertEquals(task, result)
        coVerify { taskDao.getTaskById(1) }
    }

    @Test
    fun `syncTasksWithServer should sync created and updated tasks`() = runBlocking {
        val unsyncedTasks = listOf(
            TaskEntity(0, "New Task", "Description", false, false),
            TaskEntity(1, "Updated Task", "Description", false, false)
        )
        every { taskDao.getUnsyncedTasks() } returns flowOf(unsyncedTasks)

        coEvery { apiService.createTask(unsyncedTasks[0]) } returns Response.success(unsyncedTasks[0])
        coEvery { apiService.updateTask(1, unsyncedTasks[1]) } returns Response.success(unsyncedTasks[1])
        coEvery { taskDao.update(unsyncedTasks[0].copy(isSynced = true)) } just runs
        coEvery { taskDao.update(unsyncedTasks[1].copy(isSynced = true)) } just runs
        every { taskDao.getDeletedTasks() } returns flowOf(emptyList())

        repository.syncTasksWithServer()

        coVerify { apiService.createTask(unsyncedTasks[0]) }
        coVerify { apiService.updateTask(1, unsyncedTasks[1]) }
        coVerify { taskDao.update(unsyncedTasks[0].copy(isSynced = true)) }
        coVerify { taskDao.update(unsyncedTasks[1].copy(isSynced = true)) }
    }

    @Test
    fun `syncTasksWithServer should sync deleted tasks`() = runBlocking {
        val deletedTasks = listOf(
            TaskEntity(1, "Deleted Task", "Description", false, true)
        )
        every { taskDao.getUnsyncedTasks() } returns flowOf(emptyList())
        every { taskDao.getDeletedTasks() } returns flowOf(deletedTasks)
        coEvery { apiService.deleteTask(1) } returns Response.success(Unit)
        coEvery { taskDao.deleteTask(deletedTasks[0]) } just runs

        repository.syncTasksWithServer()

        coVerify { apiService.deleteTask(1) }
        coVerify { taskDao.deleteTask(deletedTasks[0]) }
    }

    @Test
    fun `syncTasksWithServer should handle failed api calls`() = runBlocking {
        val unsyncedTasks = listOf(TaskEntity(0, "New Task", "Description", false, false))
        every { taskDao.getUnsyncedTasks() } returns flowOf(unsyncedTasks)
        val errorBody = "{\"error\":\"Bad Request\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { apiService.createTask(unsyncedTasks[0]) } returns Response.error(400, errorBody)
        every { taskDao.getDeletedTasks() } returns flowOf(emptyList())

        repository.syncTasksWithServer()

        coVerify { apiService.createTask(unsyncedTasks[0]) }
        coVerify(exactly = 0) { taskDao.update(any()) }
    }

    @Test
    fun `syncTasksWithServer should handle failed delete api calls`() = runBlocking {
        val deletedTasks = listOf(TaskEntity(1, "Deleted Task", "Description", false, true))
        every { taskDao.getUnsyncedTasks() } returns flowOf(emptyList())
        every { taskDao.getDeletedTasks() } returns flowOf(deletedTasks)
        val errorBody = "{\"error\":\"Not Found\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { apiService.deleteTask(1) } returns Response.error(404, errorBody)

        repository.syncTasksWithServer()

        coVerify { apiService.deleteTask(1) }
        coVerify(exactly = 0) { taskDao.deleteTask(any()) }
    }
}