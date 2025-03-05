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

class UpdateTaskUseCaseTest {

    @MockK
    private lateinit var repository: Repository

    private lateinit var useCase: UpdateTaskUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `invoke should call repository update with the provided task`() = runBlocking {
        val task = TaskEntity(1, "Updated Task", "Updated Description", true, true, false)
        coEvery { repository.update(task) } returns Unit

        useCase(task)

        coVerify { repository.update(task) }
    }

    @Test
    fun `invoke should handle different tasks correctly`() = runBlocking {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        coEvery { repository.update(task1) } returns Unit
        coEvery { repository.update(task2) } returns Unit

        useCase(task1)
        useCase(task2)

        coVerify { repository.update(task1) }
        coVerify { repository.update(task2) }
    }

    @Test
    fun `invoke should not throw exception when repository update succeeds`() = runBlocking {
        val task = TaskEntity(1, "Updated Task", "Updated Description", true, true, false)
        coEvery { repository.update(task) } returns Unit

        useCase(task)
    }
}