package com.guilhermekunz.gerenciadordetarefas.presentation.addtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.guilhermekunz.gerenciadordetarefas.R
import com.guilhermekunz.gerenciadordetarefas.databinding.FragmentAddTaskBinding
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
        setupInputs()
        setupCancelClickListener()
        setupSaveClickListener()
    }

    private fun setupInputs() {
        binding.etTaskTitle.addTextChangedListener {
            viewModel.setTitle(it.toString())
        }
        binding.etTaskDescription.addTextChangedListener {
            viewModel.setDescription(it.toString())
        }
        binding.cbTaskCompleted.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setChecked(isChecked)
        }
    }

    private fun setupCancelClickListener() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
        }
    }

    private fun setupSaveClickListener() {
        binding.btnSaveTask.setOnClickListener {
            viewModel.save()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}