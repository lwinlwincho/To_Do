package com.llc.todo.all_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.todo.database.TaskEntity
import com.llc.todo.databinding.FragmentAllTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTaskFragment : Fragment(), OnItemClickListener {

    private val viewModel: AllTaskViewModel by viewModels()

    private var _binding: FragmentAllTaskBinding? = null
    private val binding get() = _binding!!

    private val allTaskItemAdapter: AllTaskItemAdapter by lazy {
        AllTaskItemAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllTask()
        viewModel.taskEvent.observe(viewLifecycleOwner) { taskEvent ->
            when (taskEvent) {

                is AllTaskEvent.Success -> {
                    allTaskItemAdapter.submitList(taskEvent.taskList)
                }
                is AllTaskEvent.SuccessComplete -> {
                    if (taskEvent.message.isNotBlank()) showMessage(taskEvent.message)
                }
                is AllTaskEvent.Failure -> {
                    showMessage(taskEvent.message)
                }
                else -> {}
            }
        }

        binding.rvTask.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = allTaskItemAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            val action = AllTaskFragmentDirections
                .actionAllTaskFragmentToNewTaskFragment()
            findNavController().navigate(action)
        }
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }

    override fun onCompleteTask(taskEntity: TaskEntity) {
        viewModel.completeTask(taskEntity)
    }

    override fun openDetails(taskEntity: TaskEntity) {
        goToDetails(taskEntity)
    }

    private fun goToDetails(taskEntity: TaskEntity) {
        val action = AllTaskFragmentDirections
            .actionAllTaskFragmentToDetailTaskFragment(taskEntity.id)
        findNavController().navigate(action)
    }
}