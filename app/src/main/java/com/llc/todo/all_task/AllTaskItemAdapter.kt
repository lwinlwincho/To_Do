package com.llc.todo.all_task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.todo.database.TaskEntity
import com.llc.todo.databinding.ItemTaskBinding


interface OnItemClickListener {
    fun onCompleteTask(taskEntity: TaskEntity)
    fun openDetails(taskEntity: TaskEntity)
}

class AllTaskItemAdapter(
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<TaskEntity, AllTaskItemAdapter.AllTaskViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTaskViewHolder {

        return AllTaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: AllTaskViewHolder, position: Int) {
        val taskItem: TaskEntity = getItem(position)
        holder.bind(taskItem)
    }

    class AllTaskViewHolder(
        private var binding: ItemTaskBinding,
        private val onItemClickListener: OnItemClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            with(binding) {
                tvTitle.text = taskEntity.title
                checkBox.isChecked = taskEntity.isComplete
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onItemClickListener.onCompleteTask(taskEntity.copy(isComplete = isChecked))
                }

                tvTitle.setOnClickListener {
                    checkBox.isChecked = checkBox.isChecked == true
                    onItemClickListener.openDetails(taskEntity)
                }
            }
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<TaskEntity>() {

        override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
            return oldItem == newItem
        }
    }
}