package com.guilhermekunz.gerenciadordetarefas

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao, private val apiService: TaskApiService) {

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
        syncTasks()
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        syncTasks()
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
        syncTasks()
    }

    suspend fun syncTasks() {
        val unsyncedTasks = taskDao.getUnsyncedTasks()
        for (task in unsyncedTasks) {
            try {
                apiService.addTask(task)
                taskDao.updateTask(task.copy(isSynced = true))
            } catch (e: Exception) {
                // Mantém a tarefa sem sincronizar
            }
        }
    }
}