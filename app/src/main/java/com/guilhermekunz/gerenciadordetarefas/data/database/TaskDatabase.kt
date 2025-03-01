package com.guilhermekunz.gerenciadordetarefas.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guilhermekunz.gerenciadordetarefas.data.database.dao.TaskDao
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao
}