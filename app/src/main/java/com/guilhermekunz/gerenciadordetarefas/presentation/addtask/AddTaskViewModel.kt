package com.guilhermekunz.gerenciadordetarefas.presentation.addtask

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.data.network.NetworkResponse
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.ApiCreateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.RoomCreateTaskUseCase
import com.guilhermekunz.gerenciadordetarefas.presentation.utils.isNetworkAvailable
import kotlinx.coroutines.launch

class AddTaskViewModel(private val roomCreateTaskUseCase: RoomCreateTaskUseCase,
                       private val createTaskUseCase: ApiCreateTaskUseCase
) : ViewModel() {

    private val _taskCreationState = MutableLiveData<TaskEntity>()
    val taskCreationState: LiveData<TaskEntity> = _taskCreationState
    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> = _error

    fun apiCreateTask(task: TaskEntity, context: Context) {
        if (isNetworkAvailable(context)) {
            apiCreateTask(task)
        } else {
            roomCreateTask(task)
        }
    }

    private fun roomCreateTask(task: TaskEntity) {
            viewModelScope.launch {
                roomCreateTaskUseCase(task)
            }
    }

    private fun apiCreateTask(task: TaskEntity) {
        viewModelScope.launch {
           when (val result = createTaskUseCase.execute(task)) {
               is NetworkResponse.Error -> {
                   _error.value = Unit
               }
               is NetworkResponse.Success -> {
                   result.data.let {
                       _taskCreationState.value = it
                       roomCreateTask(it)
                   }
               }
           }
        }
    }

}