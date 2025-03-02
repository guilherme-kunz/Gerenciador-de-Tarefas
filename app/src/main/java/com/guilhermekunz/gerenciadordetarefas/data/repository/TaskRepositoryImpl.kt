package com.guilhermekunz.gerenciadordetarefas.data.repository

import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.repository.Repository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val taskDao: TaskDao): Repository {

    override suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    override fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    override suspend fun update(task: TaskEntity) {
        taskDao.update(task)
    }

    override suspend fun getTaskById(taskId: Long): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }
}