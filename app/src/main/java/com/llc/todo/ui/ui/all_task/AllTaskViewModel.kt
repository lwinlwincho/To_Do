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
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class AllTaskViewModel @Inject constructor(private val localDataSource: LocalDataSource) :
    ViewModel() {

    private val _taskEvent = MutableLiveData<AllTaskEvent>()
    val taskEvent: LiveData<AllTaskEvent> = _taskEvent

    fun getAllTask() {
        viewModelScope.launch {
            try {
                localDataSource.allTasksStream.collectLatest {
                    _taskEvent.value = AllTaskEvent.Success(it)

                    //if you use fun observeTasks()
                    // localDataSource.observeTasks().collectLatest { _taskEvent.value = AllTaskEvent.Success(it) }
                }
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }

    fun getTaskCompleted() {
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = localDataSource.getTaskByComplete()
                _taskEvent.value = AllTaskEvent.SuccessGetCompleteTask(result)
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }

    fun getTaskActive() {
        viewModelScope.launch {
            try {
                val result: List<TaskEntity> = localDataSource.getTaskByActive()
                _taskEvent.value = AllTaskEvent.SuccessGetActiveTask(result)
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
                    _taskEvent.postValue(AllTaskEvent.SuccessUpdateComplete("Task marked complete!"))
                else
                    _taskEvent.postValue(AllTaskEvent.SuccessUpdateComplete("Task marked active!"))
            } catch (e: Exception) {
                _taskEvent.postValue(AllTaskEvent.Failure(e.message.toString()))
            }
        }
    }

    fun clearCompletedTask() {
        viewModelScope.launch {
            try {
                localDataSource.clearCompleteTask()
                _taskEvent.value = AllTaskEvent.SuccessClearCompleteTask("Completed tasks cleared")
            } catch (e: Exception) {
                _taskEvent.value = AllTaskEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class AllTaskEvent {
    object Loading : AllTaskEvent()
    data class Success(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class SuccessUpdateComplete(val message: String) : AllTaskEvent()
    data class SuccessClearCompleteTask(val message: String) : AllTaskEvent()
    data class SuccessGetCompleteTask(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class SuccessGetActiveTask(val taskList: List<TaskEntity>) : AllTaskEvent()
    data class Failure(val message: String) : AllTaskEvent()
}
