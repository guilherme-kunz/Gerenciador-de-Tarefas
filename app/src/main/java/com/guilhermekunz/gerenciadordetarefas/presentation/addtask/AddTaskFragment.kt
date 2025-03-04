package com.guilhermekunz.gerenciadordetarefas.presentation.addtask

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
import com.guilhermekunz.gerenciadordetarefas.databinding.FragmentAddTaskBinding
import com.guilhermekunz.gerenciadordetarefas.domain.entity.TaskEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AddTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCancelClickListener()
        setupSaveClickListener()
    }

    private fun setupCancelClickListener() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
        }
    }

    private fun setupSaveClickListener() {
        binding.btnSaveTask.setOnClickListener {
            hideKeyboard()
            val task = TaskEntity(
                title = binding.etTaskTitle.text.toString(),
                description = binding.etTaskDescription.text.toString(),
                isChecked = binding.cbTaskCompleted.isChecked,
            )
            viewModel.roomCreateTask(task)
            Toast.makeText(requireContext(), "Tarefa salva", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
            }, 2000)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}