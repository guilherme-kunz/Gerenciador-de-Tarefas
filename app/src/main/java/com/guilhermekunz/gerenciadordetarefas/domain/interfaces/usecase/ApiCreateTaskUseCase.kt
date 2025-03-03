package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.data.network.NetworkResponse
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class ApiCreateTaskUseCase(private val repository: Repository) {

    suspend fun execute(task: TaskEntity): NetworkResponse<TaskEntity> {
        return repository.createTask(task)
    }
}