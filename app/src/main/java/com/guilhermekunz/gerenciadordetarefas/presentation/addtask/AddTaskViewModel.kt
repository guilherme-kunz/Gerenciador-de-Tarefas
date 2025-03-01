package com.guilhermekunz.gerenciadordetarefas.presentation.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilhermekunz.gerenciadordetarefas.domain.interfaces.usecase.SaveTaskUseCase
import kotlinx.coroutines.launch

class AddTaskViewModel(private val saveTaskUseCase: SaveTaskUseCase) : ViewModel() {

    private var title: String? = null
    private var description: String? = null
    private var isChecked: Boolean = false

    fun setTitle(title: String) {
        this.title = title
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setChecked(isChecked: Boolean) {
        this.isChecked = isChecked
    }

    fun save() {
        if (!title.isNullOrBlank() && !description.isNullOrBlank()) {
            viewModelScope.launch {
                saveTaskUseCase(title!!, description!!, isChecked, false)
            }
        }
    }

}