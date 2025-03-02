package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class UpdateTaskUseCase(private val repository: Repository) {
    suspend operator fun invoke(task: TaskEntity) {
        repository.updateTask(task.copy(isChecked = !task.isChecked))
    }
}