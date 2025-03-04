package com.guilhermekunz.gerenciadordetarefas.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val isChecked: Boolean,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)