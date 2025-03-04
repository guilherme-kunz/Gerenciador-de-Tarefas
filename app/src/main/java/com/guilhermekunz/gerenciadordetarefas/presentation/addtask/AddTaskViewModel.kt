package com.guilhermekunz.gerenciadordetarefas.presentation.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.RoomCreateTaskUseCase
import kotlinx.coroutines.launch

class AddTaskViewModel(private val roomCreateTaskUseCase: RoomCreateTaskUseCase) : ViewModel() {

    fun roomCreateTask(task: TaskEntity) {
        viewModelScope.launch {
            roomCreateTaskUseCase(task)
        }
    }
}