package com.dicoding.courseschedule.paging

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.util.DayName.Companion.getByNumber

class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var course: Course
    private val timeString = itemView.context.resources.getString(R.string.time_format)

    //TODO 7 : Complete ViewHolder to show item
    fun bind(course: Course, clickListener: (Course) -> Unit) {
        this.course = course

        course.apply {
            val dayName = getByNumber(day)
            val timeFormat = String.format(timeString, dayName, startTime, endTime)

            // Calculate remaining time based on the current day and start time
            val remainingTime = calculateRemainingTime(day, startTime)

            // Set the course data to the respective views
            itemView.findViewById<TextView>(R.id.tv_course).text = courseName
            itemView.findViewById<TextView>(R.id.tv_time).text = timeFormat
            itemView.findViewById<TextView>(R.id.tv_lecturer).text = lecturer

            // Set click listener to the item view
            itemView.setOnClickListener {
                clickListener(course)
            }
        }
    }
    private fun calculateRemainingTime(day: Int, startTime: String): String {
        // TODO: Implement the logic to calculate remaining time based on the current day and start time
        // You can use Calendar or other time-related APIs to calculate the remaining time

        return "" // Return the calculated remaining time as a string
    }

    fun getCourse(): Course = course
}