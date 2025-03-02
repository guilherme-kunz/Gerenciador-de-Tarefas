package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class GetTaskByIdUseCase(private val repository: Repository) {
    suspend operator fun invoke(taskId: Long): TaskEntity? {
        return repository.getTaskById(taskId)
    }
}