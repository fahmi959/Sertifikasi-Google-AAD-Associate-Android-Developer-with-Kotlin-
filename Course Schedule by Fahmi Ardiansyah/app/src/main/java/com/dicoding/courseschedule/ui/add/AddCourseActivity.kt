package com.dicoding.courseschedule.ui.add

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.CourseDatabase
import com.dicoding.courseschedule.util.DayName
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity() {

    private lateinit var dayAdapter: ArrayAdapter<String>
    private lateinit var startTimePicker: TimePickerDialog
    private lateinit var endTimePicker: TimePickerDialog
    private lateinit var selectedStartTime: Calendar
    private lateinit var selectedEndTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ibStartTime: ImageButton = findViewById(R.id.ib_start_time)
        val tvStartTime: TextView = findViewById(R.id.tv_start_time)
        val ibEndTime: ImageButton = findViewById(R.id.ib_end_time)
        val tvEndTime: TextView = findViewById(R.id.tv_end_time)

        // Inisialisasi Calendar untuk menyimpan waktu yang dipilih
        selectedStartTime = Calendar.getInstance()
        selectedEndTime = Calendar.getInstance()

        // Set up spinner adapter
        dayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
//            resources.getStringArray(R.array.day)
            DayName.values().map { it.value }
        )
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerDay: Spinner = findViewById(R.id.spinner_day)
        spinnerDay.adapter = dayAdapter

        // Buat dialog pemilih waktu untuk start
        startTimePicker = TimePickerDialog(
            this,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedStartTime.set(Calendar.MINUTE, minute)
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedStartTime.time)
                tvStartTime.text = formattedTime
            },
            selectedStartTime.get(Calendar.HOUR_OF_DAY),
            selectedStartTime.get(Calendar.MINUTE),
            true
        )

        // Buat dialog pemilih waktu untuk endTime
        endTimePicker = TimePickerDialog(
            this,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                selectedEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedEndTime.set(Calendar.MINUTE, minute)
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedEndTime.time)
                tvEndTime.text = formattedTime
            },
            selectedEndTime.get(Calendar.HOUR_OF_DAY),
            selectedEndTime.get(Calendar.MINUTE),
            true
        )

        // Set onClickListener pada ImageButton untuk menampilkan dialog pemilih waktu
        ibStartTime.setOnClickListener {
            startTimePicker.show()
        }

        tvStartTime.setOnClickListener {
            startTimePicker.show()
        }

        ibEndTime.setOnClickListener {
            endTimePicker.show()
        }

        tvEndTime.setOnClickListener {
            endTimePicker.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                insertCourse()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun insertCourse() {
        val edCourseName: TextInputEditText = findViewById(R.id.ed_course_name)




        val edLecturer: TextInputEditText = findViewById(R.id.ed_lecturer)
        val edNote: TextInputEditText = findViewById(R.id.ed_note)
        val spinnerDay: Spinner = findViewById(R.id.spinner_day)

        val courseName = edCourseName.text?.toString() ?: ""
        val day = spinnerDay.selectedItemPosition + 1 - 0 + 0 //posisi hari
        val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedStartTime.time)
        val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedEndTime.time)
        val lecturer = edLecturer.text?.toString() ?: ""
        val note = edNote.text?.toString() ?: ""

        val course = Course(
            courseName = courseName,
            day = day,
            startTime = startTime,
            endTime = endTime,
            lecturer = lecturer,
            note = note
        )

        // Mendapatkan instance CourseDatabase
        val database = CourseDatabase.getInstance(this)

        // Mendapatkan objek CourseDao dari database
        val courseDao = database.courseDao()

        // Melakukan operasi insertCourse dengan menggunakan coroutine
        CoroutineScope(Dispatchers.IO).launch {
            courseDao.insert(course)
        }

        finish()
    }
}
