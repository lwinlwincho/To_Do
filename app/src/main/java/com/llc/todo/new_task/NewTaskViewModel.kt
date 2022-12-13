package com.llc.todo.new_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.database.TaskDao
import com.llc.todo.database.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private var _newTaskUiEvent = MutableLiveData<NewTaskEvent>()
    val newTaskUiEvent: LiveData<NewTaskEvent> = _newTaskUiEvent

    fun addNewTask(
        title: String,
        task: String
    ) {
        viewModelScope.launch {
            try {
                val entity = TaskEntity(
                    title = title,
                    task = task
                )
                taskDao.insertTask(entity)
                _newTaskUiEvent.postValue(NewTaskEvent.Success("Successfully Added!"))
            } catch (e: Exception) {
                _newTaskUiEvent.postValue(NewTaskEvent.Failure(e.message.toString()))
            }
        }
    }
}

sealed class NewTaskEvent {
    data class Success(val message: String) : NewTaskEvent()
    data class Failure(val message: String) : NewTaskEvent()
}
