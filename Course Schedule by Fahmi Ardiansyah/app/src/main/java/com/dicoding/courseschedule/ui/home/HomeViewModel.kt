package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData(QueryType.CURRENT_DAY)

    fun getQueryType(): LiveData<QueryType> = _queryType

    fun setQueryType(queryType: QueryType) {
        repository.setQueryType(queryType)
    }

    fun getNearestSchedule(queryType: QueryType): LiveData<Course?> = repository.getNearestSchedule(queryType)

    fun getTodaySchedule(): LiveData<PagedList<Course>> = repository.getTodaySchedule()
}