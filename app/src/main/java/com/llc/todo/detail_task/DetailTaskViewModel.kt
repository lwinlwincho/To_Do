package com.llc.todo.detail_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.database.TaskDao
import com.llc.todo.database.TaskEntity
import com.llc.todo.singleEvent.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private val _detailUIEvent = MutableLiveData<DetailTaskEvent>()
    val detailUIEvent: LiveData<DetailTaskEvent> = _detailUIEvent

    private val _favouriteEvent = MutableLiveData<Event<DetailTaskEvent>>()
    val favouriteEvent: LiveData<Event<DetailTaskEvent>> = _favouriteEvent

    private val _favouriteStatusEvent = MutableLiveData<Event<Boolean>>()
    val favouriteStatusEvent: LiveData<Event<Boolean>> = _favouriteStatusEvent

    fun getTaskDetail(taskId: String) {
        viewModelScope.launch {
            try {
                val result = taskDao.getTaskById(taskId.toLong())
                _detailUIEvent.value = DetailTaskEvent.Success(result)
            } catch (e: Exception) {
                _detailUIEvent.value = DetailTaskEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class DetailTaskEvent {
    data class Success(val taskEntity: TaskEntity) : DetailTaskEvent()
    data class Failure(val message: String) : DetailTaskEvent()
    data class SuccessUnchecked(val message: String) : DetailTaskEvent()
    data class SuccessChecked(val message: String) : DetailTaskEvent()
}