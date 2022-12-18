package com.llc.todo.data.repository

import com.llc.todo.data.database.TaskEntity

interface LocalDataSource {

    suspend fun insertTask(item: TaskEntity)

    suspend fun updateTask(id: Long, title: String, task: String)

    suspend fun completeTask(id: Long, isComplete: Boolean)

    suspend fun delete(item: TaskEntity)

    suspend fun clearCompleteTask()

    fun getTaskByComplete(isComplete: Boolean): List<TaskEntity>

    //fun getTaskByActive(isComplete: Boolean): List<TaskEntity>

    fun getTaskById(id: Long): TaskEntity

    fun getAllTask(): List<TaskEntity>
}