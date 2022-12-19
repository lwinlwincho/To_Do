package com.llc.todo.ui.ui.new_task

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
class NewTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private var _newTaskUiEvent = MutableLiveData<NewTaskEvent>()
    val newTaskUiEvent: LiveData<NewTaskEvent> = _newTaskUiEvent

    fun isEntryValid(title: String, task: String): Boolean {
        if (title.isBlank() || task.isBlank()) {
            return false
        }
        return true
    }

    fun addNewTask(
        title: String,
        task: String
    ) {
        viewModelScope.launch {
            try {
                val entity = TaskEntity(
                    title = title,
                    task = task,
                    isComplete = false
                )
                taskDao.insertTask(entity)
                _newTaskUiEvent.postValue(NewTaskEvent.Success("Task added!"))
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
