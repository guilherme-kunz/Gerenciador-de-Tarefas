package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class DeleteTaskUseCase(private val repository: Repository) {
    suspend operator fun invoke(task: TaskEntity) {
        repository.deleteTask(task)
    }
}