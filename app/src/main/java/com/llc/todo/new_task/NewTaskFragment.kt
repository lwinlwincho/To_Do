package com.llc.todo.new_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.llc.todo.databinding.FragmentNewTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private val viewModel: NewTaskViewModel by viewModels()

    private var _binding: FragmentNewTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.newTaskUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is NewTaskEvent.Success -> {
                    showMessage(it.message)
                    findNavController().navigateUp()
                }
                is NewTaskEvent.Failure -> {
                    showMessage(it.message)
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.addNewTask(
                title = binding.etTitle.text.toString(),
                task = binding.etTask.text.toString()
            )
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ -> }
            .show()
    }
}