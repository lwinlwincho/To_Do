package com.llc.todo.ui.ui.edit_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.llc.todo.data.database.TaskEntity
import com.llc.todo.databinding.FragmentEditTaskBinding
import com.llc.todo.extension.showCustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskFragment : Fragment() {

    private var _binding: FragmentEditTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditTaskViewModel by viewModels()

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showTask(args.id.toInt())

        viewModel.editUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is EditTaskEvent.SuccessShow -> {
                    bind(it.editTaskEvent)
                }
                is EditTaskEvent.SuccessUpdate -> {
                    findNavController().navigateUp()
                    showMessage(it.message)
                }
                is EditTaskEvent.Error -> {
                    showMessage(it.error)
                }
            }
        }
    }

    private fun bind(item: TaskEntity) {
        with(binding) {
            etTitle.setText(item.title)
            etTask.setText(item.task)
            floatingActionButton.setOnClickListener { update() }
        }
    }

    private fun update() {
        if (isEntryValid()) {
            viewModel.updateTask(
                id = args.id,
                title = binding.etTitle.text.toString(),
                task = binding.etTask.text.toString()
            )
        }
        else showMessage("Please edit your tasks!")
    }

    //return true if the edit text are not empty
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.etTitle.text.toString(),
            binding.etTask.text.toString()
        )
    }

    private fun showMessage(message: String) {
       /* MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()*/

        Toast(requireContext()).showCustomToast (
            message,
            requireActivity()
        )
    }
}

