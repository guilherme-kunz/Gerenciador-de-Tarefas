package com.guilhermekunz.gerenciadordetarefas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isSynced: Boolean = false // Indica se a tarefa foi sincronizada com a API
)