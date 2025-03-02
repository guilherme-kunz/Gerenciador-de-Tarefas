package com.guilhermekunz.gerenciadordetarefas.presentation.edittask

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.guilhermekunz.gerenciadordetarefas.R
import com.guilhermekunz.gerenciadordetarefas.databinding.FragmentEditTaskBinding
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<EditTaskViewModel>()
    private val taskId: Long
        get() = requireArguments().getLong("args")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadTask(taskId)

        viewModel.task.observe(viewLifecycleOwner) { task ->
            task?.let {
                binding.etTaskTitleEdit.setText(it.title)
                binding.etTaskDescriptionEdit.setText(it.description)
                binding.cbTaskCompleted.isChecked = it.isChecked
            }
        }

        binding.btnSaveTask.setOnClickListener {
            hideKeyboard()
            val updatedTask = TaskEntity(
                id = taskId,
                title = binding.etTaskTitleEdit.text.toString(),
                description = binding.etTaskDescriptionEdit.text.toString(),
                isChecked = binding.cbTaskCompleted.isChecked,
                isSynced = false
            )
            viewModel.updateTask(updatedTask)
            Toast.makeText(requireContext(), "Tarefa atualizada", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
            }, 2000)
        }

        binding.btnCancel.setOnClickListener {
            hideKeyboard()
            findNavController().navigate(R.id.action_editTaskFragment_to_taskListFragment)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}