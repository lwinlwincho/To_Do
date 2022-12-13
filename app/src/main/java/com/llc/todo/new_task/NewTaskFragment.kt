package com.llc.todo.new_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.llc.todo.databinding.FragmentAllTaskBinding
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

        binding.floatingActionButton.setOnClickListener{
            viewModel.addNewTask(
                title = binding.etTitle.text.toString(),
                task = binding.etTask.text.toString()
            )
        }

        viewModel.newTaskUiEvent.observe(viewLifecycleOwner) {
            when (it) {
                is NewTaskEvent.Success -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is NewTaskEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}