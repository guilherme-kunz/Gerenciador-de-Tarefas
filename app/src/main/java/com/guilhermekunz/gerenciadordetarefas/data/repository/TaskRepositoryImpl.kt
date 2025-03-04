package com.guilhermekunz.gerenciadordetarefas.data.repository

import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.data.network.TaskApiService
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val taskDao: TaskDao,
    private val apiService: TaskApiService): Repository {

    override suspend fun insertTask(task: TaskEntity) {
        val taskWithSyncFlag = task.copy(isSynced = false)
        taskDao.insertTask(taskWithSyncFlag)
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    override suspend fun update(task: TaskEntity) {
        val updatedTask = task.copy(isSynced = false)
        taskDao.update(updatedTask)
    }

    override suspend fun getTaskById(taskId: Long): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }

    override suspend fun syncTasksWithServer() {
        // 1. Sincronizar tarefas criadas ou atualizadas
        val unsyncedTasks = taskDao.getUnsyncedTasks()
        unsyncedTasks.collect { tasks ->
            tasks.forEach { task ->
                val response = if (task.id == 0L) {
                    apiService.createTask(task)
                } else {
                    apiService.updateTask(task.id, task)
                }
                if (response.isSuccessful) {
                    taskDao.update(task.copy(isSynced = true))
                }
            }
        }
        // 2. Sincronizar tarefas deletadas
        val deletedTasks = taskDao.getDeletedTasks()
        deletedTasks.collect { tasks ->
            tasks.forEach { task ->
                val response = apiService.deleteTask(task.id)
                if (response.isSuccessful) {
                    taskDao.deleteTask(task) // Remove definitivamente do banco local
                }
            }
        }
    }
}