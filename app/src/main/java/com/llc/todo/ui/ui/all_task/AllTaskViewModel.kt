package com.llc.todo.ui.ui.all_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.todo.data.database.TaskEntity
import com.llc.todo.data.repository.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AllTaskViewModel @Inject constructor(private val localDataSource: LocalDataSource) :
    ViewModel() {

    private val _taskEvent = MutableLiveData<AllTaskEvent>()
    val taskEvent: LiveData<AllTaskEvent> = _taskEvent

    fun getAllTask() {
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = localDataSource.getAllTask()
                _taskEvent.value = AllTaskEvent.Success(result)
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }

    fun getTaskCompleted(isComplete: Boolean) {
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = localDataSource.getTaskByComplete(isComplete)
                _taskEvent.value = AllTaskEvent.SuccessCompleteTask(result)
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }

    fun completeTask(taskEntity: TaskEntity) {
        viewModelScope.launch {
            try {
                localDataSource.completeTask(
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

    fun clearCompletedTask(isComplete: Boolean) {
        viewModelScope.launch {
            try {
                localDataSource.clearTask(isComplete)
                _taskEvent.value = AllTaskEvent.SuccessClearCompleteTask("Completed tasks cleared")
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }


}

sealed class AllTaskEvent {
    data class Success(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class SuccessComplete(val message: String) : AllTaskEvent()
    data class SuccessClearCompleteTask(val message: String) : AllTaskEvent()
    data class SuccessCompleteTask(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class SuccessActiveTask(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class Failure(val message: String) : AllTaskEvent()
}
