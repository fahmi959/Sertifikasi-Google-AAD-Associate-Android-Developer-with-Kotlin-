package com.fahmi.dicodingtodoaad.ui.add

import androidx.lifecycle.ViewModel
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskRepository

class AddTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    suspend fun addTask(title: String, description: String, dueDateMillis: Long) {
        val newTask = Task(title = title, description = description, dueDateMillis = dueDateMillis)
        taskRepository.insertTask(newTask)
    }
}
