package com.guilhermekunz.gerenciadordetarefas.presentation.listtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.GetAllTasksUseCase

class TaskViewModel(getAllTasksUseCase: GetAllTasksUseCase) : ViewModel() {

    val tasks = getAllTasksUseCase().asLiveData()
}