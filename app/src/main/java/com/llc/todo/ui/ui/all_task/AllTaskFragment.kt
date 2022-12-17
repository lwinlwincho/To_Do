package com.llc.todo.ui.ui.all_task

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.todo.R
import com.llc.todo.data.database.TaskEntity
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

        lateinit var task: TaskEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                is AllTaskEvent.SuccessClearCompleteTask -> {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_clear_completed -> {
              //  viewModel.clearCompletedTask(task.isComplete)
                Toast.makeText(context, "ClearCompleter", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_refresh -> {
                Toast.makeText(context, "Refresh", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_all -> {
                viewModel.getAllTask()
                Toast.makeText(context, "All", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_active -> {
                Toast.makeText(context, "Active", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_completed -> {
                Toast.makeText(context, "Completed", Toast.LENGTH_LONG).show()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
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
        task = taskEntity
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