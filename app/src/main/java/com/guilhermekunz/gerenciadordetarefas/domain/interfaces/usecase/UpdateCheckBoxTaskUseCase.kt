package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class UpdateCheckBoxTaskUseCase(private val repository: Repository) {
    suspend operator fun invoke(task: TaskEntity) {
        repository.update(task.copy(isChecked = !task.isChecked))
    }
}