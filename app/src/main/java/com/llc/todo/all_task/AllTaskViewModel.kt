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

    private val _taskEvent = MutableLiveData<TaskEvent>()
    val taskEvent: LiveData<TaskEvent> = _taskEvent

    fun getAllTask(){
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = taskDao.getAllTask()
                _taskEvent.value = TaskEvent.Success(result)
            } catch (e: Exception) {
                _taskEvent.value = TaskEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class TaskEvent {
    data class Success(val taskList: List<TaskEntity>) : TaskEvent()
    data class Failure(val message: String) : TaskEvent()
}
