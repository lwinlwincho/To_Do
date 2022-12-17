package com.llc.todo.ui.ui.detail_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.data.database.TaskDao
import com.llc.todo.data.database.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private val _detailUIEvent = MutableLiveData<DetailTaskEvent>()
    val detailUIEvent: LiveData<DetailTaskEvent> = _detailUIEvent

    fun getTaskDetail(taskId: Long) {
        viewModelScope.launch {
            try {
                val result = taskDao.getTaskById(taskId)
                _detailUIEvent.value = DetailTaskEvent.Success(result)
            } catch (e: Exception) {
                _detailUIEvent.value = DetailTaskEvent.Failure(e.message.toString())
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
                    _detailUIEvent.postValue(DetailTaskEvent.SuccessComplete("Task marked complete!"))
                else
                    _detailUIEvent.postValue(DetailTaskEvent.SuccessComplete("Task marked active!"))
            } catch (e: java.lang.Exception) {
                _detailUIEvent.postValue(DetailTaskEvent.Failure(e.message.toString()))
            }
        }
    }
}

sealed class DetailTaskEvent {
    data class Success(val taskEntity: TaskEntity) : DetailTaskEvent()
    data class SuccessComplete(val message: String) : DetailTaskEvent()
    data class Failure(val message: String) : DetailTaskEvent()
}