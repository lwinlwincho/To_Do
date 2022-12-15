package com.llc.todo.all_task

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.todo.database.TaskEntity
import com.llc.todo.databinding.ItemTaskBinding


interface OnItemClickListener {
    // fun onCheck(taskEntity: TaskEntity, isCheck: Boolean)
    fun onCheckDetail(taskEntity: TaskEntity)
}

class AllTaskItemAdapter(
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<TaskEntity, AllTaskItemAdapter.AllTaskViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTaskViewHolder {

        val sharedPref =
            parent.context?.getSharedPreferences("toDoPreference", Context.MODE_PRIVATE)
        val isMyValueChecked = sharedPref!!.getBoolean("checkbox", false)

        return AllTaskViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context)),
            onItemClickListener,
            isMyValueChecked
        )
    }

    override fun onBindViewHolder(holder: AllTaskViewHolder, position: Int) {
        val taskItem: TaskEntity = getItem(position)
        holder.bind(taskItem)

        /*   holder.itemView.setOnClickListener { view ->
               val sharedPreferences: SharedPreferences = view.context
                   .getSharedPreferences("My preference", Context.MODE_PRIVATE)
              val editor = sharedPreferences.edit()
              editor.putBoolean("checkbox", holder.itemView.isChecked())
              editor.apply()
            //  Handler().postDelayed({ listener.onItemClick(items.get(position)) }, 400)*//*
        }
        holder.c.setChecked(isMyValueChecked)*/
    }

    class AllTaskViewHolder(
        private var binding: ItemTaskBinding,
        private val onItemClickListener: OnItemClickListener,
        private val ismyValueCheck: Boolean
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntity: TaskEntity) {
            with(binding) {
                tvTitle.text = taskEntity.title

                checkBox.isChecked = ismyValueCheck

                checkBox.setOnCheckedChangeListener { checkBox, isChecked ->
                    if (checkBox.isChecked) {
                        val sharedPreferences: SharedPreferences = checkBox.context
                            .getSharedPreferences("toDoPreference", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("checkbox", isChecked)
                        editor.apply()
                    }
                }

                tvTitle.setOnClickListener {
                    checkBox.isChecked = checkBox.isChecked == true
                    onItemClickListener.onCheckDetail(taskEntity)
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