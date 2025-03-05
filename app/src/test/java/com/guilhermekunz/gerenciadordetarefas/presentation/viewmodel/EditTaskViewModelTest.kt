package com.guilhermekunz.gerenciadordetarefas.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetTaskByIdUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.edittask.EditTaskViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EditTaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @MockK
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    private lateinit var viewModel: EditTaskViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = EditTaskViewModel(getTaskByIdUseCase, updateTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTask should set task LiveData with task from getTaskByIdUseCase`() {
        val taskId: Long = 1
        val task = TaskEntity(taskId, "Task 1", "Description 1", false, false, false)
        coEvery { getTaskByIdUseCase(taskId) } returns task

        val observer = mockk<Observer<TaskEntity?>>(relaxed = true)
        viewModel.task.observeForever(observer)

        viewModel.loadTask(taskId)

        coVerify { observer.onChanged(task) }
    }

    @Test
    fun `loadTask should set task LiveData with null when getTaskByIdUseCase returns null`() {
        val taskId: Long = 1
        coEvery { getTaskByIdUseCase(taskId) } returns null

        val observer = mockk<Observer<TaskEntity?>>(relaxed = true)
        viewModel.task.observeForever(observer)

        viewModel.loadTask(taskId)

        coVerify { observer.onChanged(null) }
    }

    @Test
    fun `updateTask should call updateTaskUseCase with provided task`() {
        val task = TaskEntity(1, "Updated Task", "Updated Description", true, true, false)

        viewModel.updateTask(task)

        coVerify { updateTaskUseCase(task) }
    }

    @Test
    fun `updateTask should handle different tasks correctly`() {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        viewModel.updateTask(task1)
        viewModel.updateTask(task2)

        coVerify { updateTaskUseCase(task1) }
        coVerify { updateTaskUseCase(task2) }
    }
}