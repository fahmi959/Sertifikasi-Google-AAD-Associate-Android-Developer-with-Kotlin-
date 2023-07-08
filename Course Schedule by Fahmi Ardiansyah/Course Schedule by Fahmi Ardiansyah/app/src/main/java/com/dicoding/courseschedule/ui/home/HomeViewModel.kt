package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository): ViewModel() {




    private val _queryType = MutableLiveData<QueryType>()

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun getQueryType(): LiveData<QueryType> {
        return repository.getQueryType()
    }

    fun setQueryType(queryType: QueryType) {
        repository.setQueryType(queryType)
    }

    fun getNearestSchedule(queryType: QueryType): LiveData<Course?> {
        return repository.getNearestSchedule(queryType)
    }

    fun getTodaySchedule(): LiveData<PagedList<Course>> {
        return repository.getTodaySchedule()
    }
}
