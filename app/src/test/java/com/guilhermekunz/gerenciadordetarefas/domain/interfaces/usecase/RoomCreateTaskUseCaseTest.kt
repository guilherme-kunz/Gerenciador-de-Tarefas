package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RoomCreateTaskUseCaseTest {

    @MockK
    private lateinit var repository: Repository

    private lateinit var useCase: RoomCreateTaskUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = RoomCreateTaskUseCase(repository)
    }

    @Test
    fun `invoke should call repository insertTask with the provided task`() = runBlocking {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        coEvery { repository.insertTask(task) } returns Unit

        useCase(task)

        coVerify { repository.insertTask(task) }
    }

    @Test
    fun `invoke should not throw exception when repository insertTask succeeds`() = runBlocking {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        coEvery { repository.insertTask(task) } returns Unit

        useCase(task)
    }

    @Test
    fun `invoke should handle different tasks correctly`() = runBlocking {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        coEvery { repository.insertTask(task1) } returns Unit
        coEvery { repository.insertTask(task2) } returns Unit

        useCase(task1)
        useCase(task2)

        coVerify { repository.insertTask(task1) }
        coVerify { repository.insertTask(task2) }
    }
}