package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository

class SaveTaskUseCase(private val repository: Repository) {
    suspend operator fun invoke(title: String, description: String, isChecked: Boolean, isSynced: Boolean) {
        val task = TaskEntity(title = title, description = description, isChecked = isChecked, isSynced = isSynced)
        repository.insertTask(task)
    }
}