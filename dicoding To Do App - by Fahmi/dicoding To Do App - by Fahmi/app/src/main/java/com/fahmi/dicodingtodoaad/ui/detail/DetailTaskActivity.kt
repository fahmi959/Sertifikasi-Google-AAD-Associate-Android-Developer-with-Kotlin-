package com.fahmi.dicodingtodoaad.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fahmi.dicodingtodoaad.R
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskDatabase
import com.fahmi.dicodingtodoaad.data.TaskRepository
import com.fahmi.dicodingtodoaad.utils.TASK_ID
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DetailTaskActivity : AppCompatActivity() {

    private lateinit var taskRepository: TaskRepository
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val database = TaskDatabase.getInstance(this) // Replace with your actual database class
        val tasksDao = database.taskDao() // Use the correct method name taskDao()
        taskRepository = TaskRepository(tasksDao) // Pass the tasksDao to TaskRepository

        val taskId = intent.getIntExtra(TASK_ID, -1)
        lifecycleScope.launch {
            taskRepository.getTaskById(taskId).observe(this@DetailTaskActivity) { task ->
                task?.let {
                    this@DetailTaskActivity.task = it
                    showTaskDetail()
                }
            }
        }

        val deleteButton: Button = findViewById(R.id.btn_delete_task)
        deleteButton.setOnClickListener {
            deleteTask()
        }
    }

    private fun showTaskDetail() {
        val titleTextView: TextView = findViewById(R.id.detail_ed_title)
        val descriptionTextView: TextView = findViewById(R.id.detail_ed_description)
        val dueDateTextView: TextView = findViewById(R.id.detail_ed_due_date)

        titleTextView.text = task.title
        descriptionTextView.text = task.description

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dueDate = Date(task.dueDateMillis)
        val formattedDueDate = dateFormat.format(dueDate)
        dueDateTextView.text = formattedDueDate

        // Update other UI elements with task details
    }

    private fun deleteTask() {
        lifecycleScope.launch {
            taskRepository.deleteTask(task)
            finish()
        }
    }
}
