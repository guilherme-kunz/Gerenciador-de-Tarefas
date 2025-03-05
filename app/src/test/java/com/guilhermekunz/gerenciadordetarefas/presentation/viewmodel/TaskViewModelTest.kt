package com.guilhermekunz.gerenciadordetarefas.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetAllTasksUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateCheckBoxTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.listtask.TaskViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase

    @MockK
    private lateinit var updateCheckBoxTaskUseCase: UpdateCheckBoxTaskUseCase

    @MockK
    private lateinit var updateTaskUseCase: UpdateTaskUseCase

    private lateinit var viewModel: TaskViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        val tasks = listOf(
            TaskEntity(1, "Task 1", "Description 1", false, false, false),
            TaskEntity(2, "Task 2", "Description 2", true, false, false)
        )
        every { getAllTasksUseCase() } returns flowOf(tasks)

        viewModel = TaskViewModel(getAllTasksUseCase, updateCheckBoxTaskUseCase, updateTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks LiveData should emit tasks from getAllTasksUseCase`() {
        val tasks = listOf(
            TaskEntity(1, "Task 1", "Description 1", false, false, false),
            TaskEntity(2, "Task 2", "Description 2", true, false, false)
        )

        val observer = mockk<Observer<List<TaskEntity>>>(relaxed = true)
        viewModel.tasks.observeForever(observer)

        coVerify { observer.onChanged(tasks) }
    }

    @Test
    fun `deleteTask should call updateTaskUseCase with provided task`() {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)

        viewModel.deleteTask(task)

        coVerify { updateTaskUseCase(task) }
    }

    @Test
    fun `updateTask should call updateCheckBoxTaskUseCase with provided task`() {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)

        viewModel.updateTask(task)

        coVerify { updateCheckBoxTaskUseCase(task) }
    }

    @Test
    fun `deleteTask should handle different tasks correctly`() {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        viewModel.deleteTask(task1)
        viewModel.deleteTask(task2)

        coVerify { updateTaskUseCase(task1) }
        coVerify { updateTaskUseCase(task2) }
    }

    @Test
    fun `updateTask should handle different tasks correctly`() {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        viewModel.updateTask(task1)
        viewModel.updateTask(task2)

        coVerify { updateCheckBoxTaskUseCase(task1) }
        coVerify { updateCheckBoxTaskUseCase(task2) }
    }
}