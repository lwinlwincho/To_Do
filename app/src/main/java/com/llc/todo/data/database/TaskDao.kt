package com.llc.todo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(item: TaskEntity)

    @Query("Update taskEntity Set title=:title ,task=:task Where id=:id")
    suspend fun updateTask(id: Long, title: String, task: String)

    @Query("Update taskEntity Set isComplete=:isComplete Where id=:id")
    suspend fun completeTask(id: Long, isComplete: Boolean)

    @Delete
    suspend fun deleteTask(item: TaskEntity)

    @Query("Delete from taskEntity Where isComplete= 1")
    suspend fun clearCompleteTask()

    @Query("Select * from taskEntity Where id= :id")
    fun getTaskById(id: Long): TaskEntity

    @Query("Select * from taskEntity Where isComplete= 1")
    fun getTaskByComplete(): List<TaskEntity>

    @Query("Select * from taskEntity Where isComplete= 0")
    fun getTaskByActive(): List<TaskEntity>

    @Query("Select * from taskEntity")
    fun observeTasks(): Flow<List<TaskEntity>>
}