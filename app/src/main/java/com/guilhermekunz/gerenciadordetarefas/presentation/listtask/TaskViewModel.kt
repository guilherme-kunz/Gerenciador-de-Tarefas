package com.guilhermekunz.gerenciadordetarefas.presentation.listtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetAllTasksUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateCheckBoxTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import kotlinx.coroutines.launch

class TaskViewModel(getAllTasksUseCase: GetAllTasksUseCase,
                    private val updateCheckBoxTaskUseCase: UpdateCheckBoxTaskUseCase,
                    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    val tasks = getAllTasksUseCase().asLiveData()

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            updateCheckBoxTaskUseCase(task)
        }
    }
}