package com.dicoding.courseschedule.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2: Define data access object (DAO)
@Dao
interface CourseDao {


    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourse(id: Int): LiveData<Course>

    @Query("SELECT * FROM course WHERE day = :day")
    fun getTodaySchedule(day: Int): DataSource.Factory<Int, Course>

    @Query("SELECT * FROM course WHERE day = :day ORDER BY startTime ASC")
    fun getNearestSchedule(day: Int): LiveData<Course?>

    @Query("SELECT * FROM course")
    fun getAll(): DataSource.Factory<Int, Course>

    @Query("SELECT * FROM course ORDER BY courseName ASC")
    fun getAllCoursesSortedByCourseName(): DataSource.Factory<Int, Course>

    @Query("SELECT * FROM course ORDER BY startTime ASC")
    fun getAllCoursesSortedByTime(): DataSource.Factory<Int, Course>

    @Query("SELECT * FROM course ORDER BY lecturer ASC")
    fun getAllCoursesSortedByLecturer(): DataSource.Factory<Int, Course>


    @Insert
    fun insert(course: Course)

    @Delete
    fun delete(course: Course)
}