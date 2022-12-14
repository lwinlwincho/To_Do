package com.llc.todo.all_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.database.TaskDao
import com.llc.todo.database.TaskEntity
import com.llc.todo.detail_task.DetailTaskEvent
import com.llc.todo.singleEvent.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AllTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private val _taskEvent = MutableLiveData<AllTaskEvent>()
    val taskEvent: LiveData<AllTaskEvent> = _taskEvent

    fun getAllTask(){
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = taskDao.getAllTask()
                _taskEvent.value = AllTaskEvent.Success(result)
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class AllTaskEvent {
    data class Success(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class Failure(val message: String) : AllTaskEvent()
}
