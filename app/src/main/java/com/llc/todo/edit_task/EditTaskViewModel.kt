package com.llc.todo.edit_task

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
class EditTaskViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private var _editUiEvent = MutableLiveData<EditTaskEvent>()
    val editUiEvent: LiveData<EditTaskEvent> = _editUiEvent

    fun showTask(id: Int) {
        viewModelScope.launch {
            try {
                val result = taskDao.getTaskById(id.toLong())
                _editUiEvent.value = EditTaskEvent.SuccessShow(result)
            } catch (e: Exception) {
                _editUiEvent.value = EditTaskEvent.Error(e.message.toString())
            }
        }
    }

    fun isEntryValid(title: String, task: String): Boolean {
        if (title.isBlank() || task.isBlank()) {
            return false
        }
        return true
    }

    fun updateTask(
        id: Long,
        title: String,
        task: String
    ) {
        viewModelScope.launch {
            try {
                taskDao.updateTask(id, title, task)
                _editUiEvent.postValue(EditTaskEvent.SuccessUpdate("Task saved!"))
            } catch (e: Exception) {
                _editUiEvent.postValue(EditTaskEvent.Error(e.message.toString()))
            }
        }
    }
}

sealed class EditTaskEvent {
    data class SuccessShow(val editTaskEvent: TaskEntity) : EditTaskEvent()
    data class SuccessUpdate(val message: String) : EditTaskEvent()
    data class Error(val error: String) : EditTaskEvent()

}