package com.guilhermekunz.gerenciadordetarefas.presentation.listtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.guilhermekunz.gerenciadordetarefas.R
import com.guilhermekunz.gerenciadordetarefas.databinding.FragmentTaskListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TaskViewModel>()

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeTasks()
        setupAddTaskButton()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter { taskId ->
//            val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment(taskId)
//            findNavController().navigate(action)
        }
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter
    }

    private fun observeTasks() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }
    }

    private fun setupAddTaskButton() {
        binding.fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}