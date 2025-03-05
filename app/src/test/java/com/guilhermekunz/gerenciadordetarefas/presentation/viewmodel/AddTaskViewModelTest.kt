package com.guilhermekunz.gerenciadordetarefas.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.RoomCreateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.addtask.AddTaskViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddTaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var roomCreateTaskUseCase: RoomCreateTaskUseCase

    private lateinit var viewModel: AddTaskViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AddTaskViewModel(roomCreateTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `roomCreateTask should call roomCreateTaskUseCase with provided task`() {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)

        viewModel.roomCreateTask(task)

        coVerify { roomCreateTaskUseCase(task) }
    }

    @Test
    fun `roomCreateTask should handle different tasks correctly`() {
        val task1 = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        val task2 = TaskEntity(2, "Task 2", "Description 2", true, false, false)

        viewModel.roomCreateTask(task1)
        viewModel.roomCreateTask(task2)

        coVerify { roomCreateTaskUseCase(task1) }
        coVerify { roomCreateTaskUseCase(task2) }
    }

    @Test
    fun `roomCreateTask should launch coroutine in viewModelScope`() {
        val task = TaskEntity(1, "Task 1", "Description 1", false, false, false)
        var coroutineLaunched = false

        val originalScope = viewModel.viewModelScope
        viewModel.viewModelScope.launch {
            coroutineLaunched = true
        }

        viewModel.roomCreateTask(task)

        assert(coroutineLaunched)
        viewModel.viewModelScope.cancel()
    }
}