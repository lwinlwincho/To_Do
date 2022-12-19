package com.llc.todo.data.repository

import com.llc.todo.data.database.TaskEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun insertTask(item: TaskEntity)

    suspend fun updateTask(id: Long, title: String, task: String)

    suspend fun completeTask(id: Long, isComplete: Boolean)

    suspend fun delete(item: TaskEntity)

    suspend fun clearCompleteTask()

    fun getTaskById(id: Long): TaskEntity

    fun getTaskByComplete(): List<TaskEntity>

    fun getTaskByActive(): List<TaskEntity>

    val allTasksStream: Flow<List<TaskEntity>>

   // you can either val allTasksStream or fun observeTasks().The results are the same.
   // fun observeTasks(): Flow<List<TaskEntity>>
}