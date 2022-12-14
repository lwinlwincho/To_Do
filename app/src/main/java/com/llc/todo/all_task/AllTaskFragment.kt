package com.llc.todo.all_task

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    /*private val allTaskItemAdapter: AllTaskItemAdapter by lazy {
        AllTaskItemAdapter { taskEntity ->
            goToDetails(taskEntity)
        }
    }*/

    private val sharedPref by lazy {
        context?.getSharedPreferences("toDoPreference", Context.MODE_PRIVATE)
    }

    companion object {
        const val KEY_CHECK = "key-check"
    }


    private val allTaskItemAdapter: AllTaskItemAdapter by lazy {
        AllTaskItemAdapter(this)
    }

    private fun goToDetails(taskEntity: TaskEntity) {
        val action = AllTaskFragmentDirections
            .actionAllTaskFragmentToDetailTaskFragment(taskEntity.id)
        findNavController().navigate(action)
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

        getCheck()

        viewModel.getAllTask()
        viewModel.taskEvent.observe(viewLifecycleOwner) { taskEvent ->
            when (taskEvent) {

                is AllTaskEvent.Success -> {
                    allTaskItemAdapter.submitList(taskEvent.taskList)
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

    private fun putCheck(isCheck: Boolean) {
        sharedPref?.edit()?.putBoolean(KEY_CHECK, isCheck)?.apply()
    }

    private fun getCheck(): Boolean {
        return sharedPref!!.getBoolean(KEY_CHECK, false)
    }

    override fun onCheck(taskEntity: TaskEntity, isCheck: Boolean) {

       /* val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean("YOUR_CHECKBOX_KEY", isCheck)
            apply()
        }*/

        putCheck(isCheck)
        Toast.makeText(requireContext(), taskEntity.title.toString()+ "true", Toast.LENGTH_LONG).show()
    }

    override fun onCheckDetail(taskEntity: TaskEntity) {
        goToDetails(taskEntity)
        Toast.makeText(requireContext(), taskEntity.title.toString()+"false", Toast.LENGTH_LONG).show()
    }
}