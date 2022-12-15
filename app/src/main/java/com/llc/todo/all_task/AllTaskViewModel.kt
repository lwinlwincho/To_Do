package com.llc.todo.all_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.database.TaskDao
import com.llc.todo.database.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AllTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private val _taskEvent = MutableLiveData<AllTaskEvent>()
    val taskEvent: LiveData<AllTaskEvent> = _taskEvent

    fun getAllTask() {
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = taskDao.getAllTask()
                _taskEvent.value = AllTaskEvent.Success(result)
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }

    fun completeTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            try {
                taskDao.completeTask(
                    id = taskEntity.id,
                    isComplete = taskEntity.isComplete
                )
                if (taskEntity.isComplete)
                    _taskEvent.postValue(AllTaskEvent.SuccessComplete("Task marked complete!"))
                else
                    _taskEvent.postValue(AllTaskEvent.SuccessComplete("Task marked active!"))
            } catch (e: Exception) {
                _taskEvent.postValue(AllTaskEvent.Failure(e.message.toString()))
            }
        }
    }
}

sealed class AllTaskEvent {
    data class Success(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class SuccessComplete(val message: String) : AllTaskEvent()
    data class Failure(val message: String) : AllTaskEvent()
}
