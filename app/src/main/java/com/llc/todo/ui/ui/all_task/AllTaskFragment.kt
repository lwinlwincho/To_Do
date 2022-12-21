package com.llc.todo.ui.ui.all_task

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.llc.todo.AllTaskItemAdapter
import com.llc.todo.OnItemClickListener
import com.llc.todo.R
import com.llc.todo.data.database.TaskEntity
import com.llc.todo.databinding.FragmentAllTaskBinding
import com.llc.todo.extension.showCustomToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AllTaskFragment : Fragment(), OnItemClickListener {

    private val viewModel: AllTaskViewModel by viewModels()

    private var _binding: FragmentAllTaskBinding? = null
    private val binding get() = _binding!!

    private val allTaskItemAdapter: AllTaskItemAdapter by lazy {
        AllTaskItemAdapter(this)
    }

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

                is AllTaskEvent.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is AllTaskEvent.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (taskEvent.taskList.isEmpty()) showMessage("You have no tasks!")
                    allTaskItemAdapter.submitList(taskEvent.taskList)
                }

                is AllTaskEvent.SuccessGetCompleteTask -> {
                    binding.progressBar.visibility = View.GONE
                    if (taskEvent.taskList.isEmpty()) showMessage("You have no completed tasks!")
                    else allTaskItemAdapter.submitList(taskEvent.taskList)
                }

                is AllTaskEvent.SuccessGetActiveTask -> {
                    binding.progressBar.visibility = View.GONE
                    if (taskEvent.taskList.isEmpty()) showMessage("You have no active tasks!")
                    else allTaskItemAdapter.submitList(taskEvent.taskList)
                }

                is AllTaskEvent.SuccessUpdateComplete -> {
                    binding.progressBar.visibility = View.GONE
                    if (taskEvent.message.isNotBlank()) showMessage(taskEvent.message)
                }

                is AllTaskEvent.SuccessClearCompleteTask -> {
                    binding.progressBar.visibility = View.GONE
                    if (taskEvent.message.isNotBlank()) showMessage(taskEvent.message)
                }

                is AllTaskEvent.Failure -> {
                    binding.progressBar.visibility = View.GONE
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
                viewModel.clearCompletedTask()
                return true
            }
            R.id.action_refresh -> {
                viewModel.getAllTask()
                return true
            }
            R.id.action_all -> {
                viewModel.getAllTask()
                return true
            }
            R.id.action_active -> {
                viewModel.getTaskActive()
                return true
            }
            R.id.action_completed -> {
                viewModel.getTaskCompleted()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showMessage(message: String) {
        /*MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()*/

        Toast(requireContext()).showCustomToast (
            message,
            requireActivity()
        )
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