package com.llc.todo.detail_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.llc.todo.R
import com.llc.todo.all_task.AllTaskFragmentDirections
import com.llc.todo.database.TaskEntity
import com.llc.todo.databinding.FragmentAllTaskBinding
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
    ): View? {
        _binding = FragmentDetailTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTaskDetail(args.id.toString())
        viewModel.detailUIEvent.observe(viewLifecycleOwner) {
            when (it) {
                is DetailTaskEvent.Success -> {
                    bind(it.taskEntity)
                }
                is DetailTaskEvent.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun bind(item: TaskEntity) {
        with(binding) {
            title.text = item.title
            task.text = item.task

            floatingActionButton.setOnClickListener{
                val action = DetailTaskFragmentDirections
                    .actionDetailTaskFragmentToEditTaskFragment(item.id.toLong())
                findNavController().navigate(action)
            }
        }
    }
}