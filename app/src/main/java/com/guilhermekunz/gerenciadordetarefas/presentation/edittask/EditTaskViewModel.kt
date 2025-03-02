package com.guilhermekunz.gerenciadordetarefas.presentation.edittask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetTaskByIdUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.UpdateTaskUseCase
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    private val _task = MutableLiveData<TaskEntity?>()
    val task: LiveData<TaskEntity?> get() = _task

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            _task.value = getTaskByIdUseCase(taskId)
        }
    }

    fun updateTask(updatedTask: TaskEntity) {
        viewModelScope.launch {
            updateTaskUseCase(updatedTask)
        }
    }
}