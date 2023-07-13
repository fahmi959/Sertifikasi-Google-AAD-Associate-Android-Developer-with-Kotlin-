package com.fahmi.dicodingtodoaad.ui

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.fahmi.dicodingtodoaad.R
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskRepository
import com.fahmi.dicodingtodoaad.utils.Event
import com.fahmi.dicodingtodoaad.utils.TasksFilterType
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {


    private val _filter = MutableLiveData<TasksFilterType>()
    val filterType: LiveData<TasksFilterType> = _filter

    val tasks: LiveData<PagedList<Task>> = _filter.switchMap { filterType ->
        taskRepository.getTasks(filterType)
    }


    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    init {
        _filter.value = TasksFilterType.ALL_TASKS
    }


    fun filter(filterType: TasksFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) {
            _snackbarText.value = Event(R.string.task_marked_complete)
        } else {
            _snackbarText.value = Event(R.string.task_marked_active)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}
