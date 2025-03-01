package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun insertTask(task: TaskEntity)

    fun getAllTasks(): Flow<List<TaskEntity>>
}