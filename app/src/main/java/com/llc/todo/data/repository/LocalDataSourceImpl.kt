package com.llc.todo.data.repository

import com.llc.todo.data.database.TaskDao
import com.llc.todo.data.database.TaskEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val taskDao: TaskDao) : LocalDataSource {

    override suspend fun insertTask(item: TaskEntity) {
        taskDao.insertTask(item)
    }

    override suspend fun updateTask(id: Long, title: String, task: String) {
        taskDao.updateTask(id, title, task)
    }

    override suspend fun completeTask(id: Long, isComplete: Boolean) {
        taskDao.completeTask(id, isComplete)
    }

    override suspend fun delete(item: TaskEntity) {
        taskDao.deleteTask(item)
    }

    override suspend fun clearTask(isComplete: Boolean) {
        taskDao.clearTask(isComplete)
    }

    override fun getTaskById(id: Long): TaskEntity {
        return taskDao.getTaskById(id)
    }

    override fun getTaskByComplete(isComplete: Boolean): List<TaskEntity> {
        return taskDao.getTaskByComplete(isComplete)
    }

   /* override fun getTaskByActive(isComplete: Boolean): List<TaskEntity> {
        return taskDao.getTaskByActive(isComplete)
    }*/

    override fun getAllTask(): List<TaskEntity> {
        return taskDao.getAllTask()
    }
}