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

class UpdateCheckBoxTaskUseCaseTest {

    @MockK
    private lateinit var repository: Repository

    private lateinit var useCase: UpdateCheckBoxTaskUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = UpdateCheckBoxTaskUseCase(repository)
    }

    @Test
    fun `invoke should call repository update with task with toggled isChecked`() = runBlocking {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val updatedTask = task.copy(isChecked = true)
        coEvery { repository.update(updatedTask) } returns Unit

        useCase(task)

        coVerify { repository.update(updatedTask) }
    }

    @Test
    fun `invoke should toggle isChecked from true to false`() = runBlocking {
        val task = TaskEntity(1, "Task 1", "Description 1", true, false, false)
        val updatedTask = task.copy(isChecked = false)
        coEvery { repository.update(updatedTask) } returns Unit

        useCase(task)

        coVerify { repository.update(updatedTask) }
    }

    @Test
    fun `invoke should handle different tasks correctly`() = runBlocking {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val updatedTask1 = task1.copy(isChecked = true)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)
        val updatedTask2 = task2.copy(isChecked = false)

        coEvery { repository.update(updatedTask1) } returns Unit
        coEvery { repository.update(updatedTask2) } returns Unit

        useCase(task1)
        useCase(task2)

        coVerify { repository.update(updatedTask1) }
        coVerify { repository.update(updatedTask2) }
    }
}