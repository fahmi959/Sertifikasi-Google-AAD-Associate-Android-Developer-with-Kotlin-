package com.fahmi.dicodingtodoaad.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fahmi.dicodingtodoaad.R
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.data.TaskDatabase
import com.fahmi.dicodingtodoaad.data.TaskRepository
import com.fahmi.dicodingtodoaad.databinding.ActivityAddTaskBinding
import com.fahmi.dicodingtodoaad.ui.ViewModelFactory
import com.fahmi.dicodingtodoaad.utils.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var addTaskViewModel: AddTaskViewModel
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.add_task)

        val tasksDao = TaskDatabase.getInstance(application).taskDao()
        val taskRepository = TaskRepository.getInstance(application)
        addTaskViewModel = ViewModelProvider(this, ViewModelFactory(taskRepository))
            .get(AddTaskViewModel::class.java)

        binding.addTvDueDate.setOnClickListener {
            showDatePicker()
        }

        binding.addIvDueDate.setOnClickListener {
            showDatePicker()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTask() {
        val title = binding.addEdTitle.text.toString()
        val description = binding.addEdDescription.text.toString()

        if (title.isNotEmpty() && description.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                addTaskViewModel.addTask(title, description, dueDateMillis)
                finish()
            }
        } else {
            showToast("Please enter title and description")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                onDateSet(year, monthOfYear, dayOfMonth)
            },
            currentYear,
            currentMonth,
            currentDayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        try {
            datePickerDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.addTvDueDate.text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }
}