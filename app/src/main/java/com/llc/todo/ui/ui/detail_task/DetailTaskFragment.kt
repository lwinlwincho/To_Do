package com.llc.todo.ui.ui.detail_task

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.llc.todo.R
import com.llc.todo.data.database.TaskEntity
import com.llc.todo.databinding.FragmentDetailTaskBinding
import com.llc.todo.extension.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTaskFragment : Fragment() {

    private var _binding: FragmentDetailTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTaskViewModel by viewModels()

    private val args: DetailTaskFragmentArgs by navArgs()

    lateinit var task: TaskEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTaskDetail(args.id)
        viewModel.detailUIEvent.observe(viewLifecycleOwner) {
            when (it) {
                is DetailTaskEvent.Success -> {
                    bind(it.taskEntity)
                    task=it.taskEntity
                }
                is DetailTaskEvent.SuccessComplete -> {
                    showMessage(it.message)
                }
                is DetailTaskEvent.SuccessDelete -> {
                    showMessage(it.message)
                    findNavController().navigateUp()
                }
                is DetailTaskEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_delete -> {
                viewModel.deleteTask(task)
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun bind(item: TaskEntity) {

        with(binding) {
            tvTitle.text = item.title
            tvTask.text = item.task
            checkBox.isChecked = item.isComplete

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.completeTask(item.copy(isComplete = isChecked))
            }

            floatingActionButton.setOnClickListener {
                val action = DetailTaskFragmentDirections
                    .actionDetailTaskFragmentToEditTaskFragment(item.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun showMessage(message: String) {
        Toast(requireContext()).showCustomToast (
            message,
            requireActivity()
        )
    }
}