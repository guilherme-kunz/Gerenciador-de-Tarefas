package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(private val repository: Repository) {
    operator fun invoke(): Flow<List<TaskEntity>> {
        return repository.getAllTasks()
    }
}