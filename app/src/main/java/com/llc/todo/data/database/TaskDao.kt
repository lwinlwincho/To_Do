package com.llc.todo.data.database

import androidx.room.*

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

    @Query("Select * from taskEntity Where isComplete= :isComplete")
    fun getTaskByComplete(isComplete: Boolean): List<TaskEntity>

    /*  @Query("Select * from taskEntity Where isComplete!= :isComplete")
      fun getTaskByActive(isComplete: Boolean): List<TaskEntity>
  */

    @Query("Select * from taskEntity Where id= :id")
    fun getTaskById(id: Long): TaskEntity

    @Query("Select * from taskEntity")
    fun getAllTask(): List<TaskEntity>
}