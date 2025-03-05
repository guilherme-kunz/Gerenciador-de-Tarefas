package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllTasksUseCaseTest {

    @MockK
    private lateinit var repository: Repository

    private lateinit var useCase: GetAllTasksUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetAllTasksUseCase(repository)
    }

    @Test
    fun `invoke should return flow of tasks from repository`() = runBlocking {
        val tasks = listOf(
            TaskEntity(1, "Task 1", "Description 1", false, false, false),
            TaskEntity(2, "Task 2", "Description 2", true, false, false)
        )
        every { repository.getAllTasks() } returns flowOf(tasks)

        val result = useCase().toList()

        assertEquals(tasks, result)
    }

    @Test
    fun `invoke should return empty flow when repository returns empty flow`() = runBlocking {
        every { repository.getAllTasks() } returns flowOf(emptyList())

        val result = useCase().toList()

        assertEquals(emptyList<TaskEntity>(), result)
    }

    @Test
    fun `invoke should return flow with single task when repository returns single task`() = runBlocking {
        val task = TaskEntity(1, "Single Task", "Description", false, false, false)
        every { repository.getAllTasks() } returns flowOf(listOf(task))

        val result = useCase().toList()

        assertEquals(listOf(task), result)
    }
}