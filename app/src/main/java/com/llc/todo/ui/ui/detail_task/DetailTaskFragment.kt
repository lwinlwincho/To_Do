package com.llc.todo.ui.ui.detail_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.todo.data.database.TaskEntity
import com.llc.todo.databinding.FragmentDetailTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTaskFragment : Fragment() {

    private var _binding: FragmentDetailTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTaskViewModel by viewModels()

    private val args: DetailTaskFragmentArgs by navArgs()

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

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
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

            imvDelete.setOnClickListener{
                viewModel.deleteTask(item)
            }
        }
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }
}