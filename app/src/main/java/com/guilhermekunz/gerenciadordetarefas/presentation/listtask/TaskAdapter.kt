package com.guilhermekunz.gerenciadordetarefas.presentation.listtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guilhermekunz.gerenciadordetarefas.databinding.TaskItemBinding
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity


class TaskAdapter(private val onTaskClick: (Long) -> Unit) :
    ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity) {
            binding.textViewTaskTitle.text = task.title
            binding.checkBoxCompleted.isChecked = task.isChecked
            binding.root.setOnClickListener { onTaskClick(task.id) }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
    override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean = oldItem == newItem
}