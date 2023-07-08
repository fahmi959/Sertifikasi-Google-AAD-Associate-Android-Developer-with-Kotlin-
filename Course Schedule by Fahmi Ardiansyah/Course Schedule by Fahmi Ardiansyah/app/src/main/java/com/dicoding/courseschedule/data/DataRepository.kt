package com.dicoding.courseschedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil.nearestQuery
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao, private val context: Context) {

    private val _queryType = MutableLiveData<QueryType>()

    fun getQueryType(): LiveData<QueryType> {
        return _queryType
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }

    private fun getQueryDay(queryType: QueryType): Int {
        return when (queryType) {
            QueryType.CURRENT_DAY -> getDayOfWeek()
            QueryType.NEXT_DAY -> getNextDayOfWeek()
            QueryType.PAST_DAY -> getPastDayOfWeek()
        }
    }

    private fun getDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun getNextDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun getPastDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, -1)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getNearestSchedule(queryType: QueryType): LiveData<Course?> {
        val day = getQueryDay(queryType)
        return dao.getNearestSchedule(day)
    }





    fun getTodaySchedule(): LiveData<PagedList<Course>> {
        val dayOfWeek = getDayOfWeek()
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(dao.getTodaySchedule(dayOfWeek), config).build()
    }






    fun getAllCourse(sortType: SortType): LiveData<PagedList<Course>> {
        val dataSourceFactory: DataSource.Factory<Int, Course> = when (sortType) {
            SortType.COURSE_NAME -> dao.getAllCoursesSortedByCourseName()
            SortType.TIME -> dao.getAllCoursesSortedByTime()
            SortType.LECTURER -> dao.getAllCoursesSortedByLecturer()
        }

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(dataSourceFactory, config).build()
    }









    fun getCourse(id: Int): LiveData<Course> {
        return dao.getCourse(id)
    }




    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context, dao: CourseDao): DataRepository {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    instance = DataRepository(dao, context)
                }
                return instance!!
            }
        }

    }
}