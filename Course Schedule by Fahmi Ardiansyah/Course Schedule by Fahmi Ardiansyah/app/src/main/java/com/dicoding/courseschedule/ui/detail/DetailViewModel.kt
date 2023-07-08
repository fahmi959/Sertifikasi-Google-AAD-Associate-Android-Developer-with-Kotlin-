package com.dicoding.courseschedule.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository

class DetailViewModel(private val repository: DataRepository, private val id: Int) : ViewModel() {

    val course: LiveData<Course> = repository.getCourse(id)

    fun deleteCourse() {
        course.value?.let {
            repository.delete(it)
        }
    }

    fun initCourse() {
        // Lakukan inisialisasi atau pengambilan data tambahan di sini
    }
}