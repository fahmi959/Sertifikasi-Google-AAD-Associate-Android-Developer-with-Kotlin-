package com.fahmi.dicodingtodoaad.ui.detail

import androidx.lifecycle.*
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskRepository
import kotlinx.coroutines.launch

class DetailTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _taskId = MutableLiveData<Int?>()

    private val _task = _taskId.switchMap { id ->
        id?.let { taskId ->
            taskRepository.getTaskById(taskId)
        } ?: MutableLiveData<Task>()
    }
    val task: LiveData<Task> = _task

    fun setTaskId(taskId: Int?) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
    }

    fun deleteTask() {
        viewModelScope.launch {
            _task.value?.let { taskRepository.deleteTask(it) }
        }
    }
}
