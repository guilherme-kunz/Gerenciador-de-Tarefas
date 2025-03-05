package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetTaskByIdUseCaseTest {

    @MockK
    private lateinit var repository: Repository

    private lateinit var useCase: GetTaskByIdUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetTaskByIdUseCase(repository)
    }

    @Test
    fun `invoke should return task when repository returns task`() = runBlocking {
        val taskId: Long = 1
        val task = TaskEntity(taskId, "Task 1", "Description 1", false, false, false)
        coEvery { repository.getTaskById(taskId) } returns task

        val result = useCase(taskId)

        assertEquals(task, result)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runBlocking {
        val taskId: Long = 1
        coEvery { repository.getTaskById(taskId) } returns null

        val result = useCase(taskId)

        assertNull(result)
    }

    @Test
    fun `invoke should return different tasks based on different task IDs`() = runBlocking {
        val taskId1: Long = 1
        val task1 = TaskEntity(taskId1, "Task 1", "Description 1", false, false, false)
        coEvery { repository.getTaskById(taskId1) } returns task1

        val taskId2: Long = 2
        val task2 = TaskEntity(taskId2, "Task 2", "Description 2", true, false, false)
        coEvery { repository.getTaskById(taskId2) } returns task2

        val result1 = useCase(taskId1)
        val result2 = useCase(taskId2)

        assertEquals(task1, result1)
        assertEquals(task2, result2)
    }
}