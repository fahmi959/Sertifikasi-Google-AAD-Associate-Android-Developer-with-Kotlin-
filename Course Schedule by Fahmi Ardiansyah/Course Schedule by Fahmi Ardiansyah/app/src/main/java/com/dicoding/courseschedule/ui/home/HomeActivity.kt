package com.dicoding.courseschedule.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.CourseDatabase
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.paging.CourseAdapter
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.detail.DetailActivity
import com.dicoding.courseschedule.ui.list.ListActivity
import com.dicoding.courseschedule.ui.setting.SettingsActivity
import com.dicoding.courseschedule.util.DayName
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.timeDifference
// TODO 15 : Write UI test to validate when user tap Add Course (+) Menu, the AddCourseActivity is displayed
class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private var queryType = QueryType.CURRENT_DAY
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var dailyReminder: DailyReminder


    // TODO 5 : Show today schedule in CardHomeView and implement menu action
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = resources.getString(R.string.today_schedule)

        val courseDao = CourseDatabase.getInstance(applicationContext).courseDao()
        val repository = DataRepository.getInstance(applicationContext, courseDao)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        viewModel.getQueryType().observe(this, { type ->
            queryType = type
            viewModel.getNearestSchedule(queryType).observe(this, { course ->
                showTodaySchedule(course)
            })
        })

        courseAdapter = CourseAdapter(this) { course ->
            onCourseClick(course)
        }

        viewModel.getTodaySchedule().observe(this, { courses ->
            showTodaySchedule(courses.firstOrNull())
            courseAdapter.submitList(courses as PagedList<Course>?)
        })

        dailyReminder = DailyReminder()

        val btnAddCourse = findViewById<Button>(R.id.btn_add_course)
        btnAddCourse.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddCourseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onCourseClick(course: Course) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("course_id", course.id)
        startActivity(intent)
    }

    private fun showTodaySchedule(course: Course?) {
        checkQueryType(course)
        course?.apply {
            val dayName = DayName.getByNumber(day)
            val time = String.format(getString(R.string.time_format), dayName, startTime, endTime)
            val remainingTime = timeDifference(day, startTime)

            val cardHome = findViewById<CardHomeView>(R.id.view_home)
            cardHome.setCourseName(courseName)
            cardHome.setTime(time)
            cardHome.setRemainingTime(remainingTime)
            cardHome.setLecturer(lecturer)
        }

        findViewById<TextView>(R.id.tv_empty_home).visibility =
            if (course == null) View.VISIBLE else View.GONE
    }

    private fun checkQueryType(course: Course?) {
        if (course == null) {
            val newQueryType: QueryType = when (queryType) {
                QueryType.CURRENT_DAY -> QueryType.NEXT_DAY
                QueryType.NEXT_DAY -> QueryType.PAST_DAY
                else -> QueryType.CURRENT_DAY
            }
            viewModel.setQueryType(newQueryType)
            queryType = newQueryType
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent? = when (item.itemId) {
            R.id.action_add -> Intent(this, AddCourseActivity::class.java)
            R.id.action_list -> Intent(this, ListActivity::class.java)
            R.id.action_settings -> Intent(this, SettingsActivity::class.java)
            else -> null
        }

        if (intent != null) {
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}

