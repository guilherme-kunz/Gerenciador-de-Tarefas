package com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository

import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun insertTask(task: TaskEntity)

    fun getAllTasks(): Flow<List<TaskEntity>>

    suspend fun deleteTask(task: TaskEntity)

    suspend fun update(task: TaskEntity)

    suspend fun getTaskById(taskId: Long): TaskEntity?

    suspend fun syncTasksWithServer()
}