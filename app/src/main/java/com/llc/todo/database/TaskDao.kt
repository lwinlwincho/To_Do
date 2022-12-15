package com.llc.todo.database

import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(item: TaskEntity)

    @Query("Update taskEntity Set title=:title ,task=:task Where id=:id")
    suspend fun updateTask(id: Long, title: String, task: String)

    @Query("Update taskEntity Set isComplete=:isComplete Where id=:id")
    suspend fun completeTask(id: Long, isComplete: Boolean)

    @Delete
    fun delete(item: TaskEntity)

    @Query("Select * from taskEntity Where id= :id")
    fun getTaskById(id: Long): TaskEntity

    @Query("Select * from taskEntity")
    fun getAllTask(): List<TaskEntity>
}