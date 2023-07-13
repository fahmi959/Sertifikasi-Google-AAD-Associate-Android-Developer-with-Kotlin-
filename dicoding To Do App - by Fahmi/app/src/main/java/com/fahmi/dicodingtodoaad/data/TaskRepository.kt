package com.fahmi.dicodingtodoaad.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fahmi.dicodingtodoaad.utils.FilterUtils
import com.fahmi.dicodingtodoaad.utils.TasksFilterType





    class TaskRepository(private val tasksDao: TaskDao) {

        companion object {
            const val PAGE_SIZE = 30
            const val PLACEHOLDERS = true

            @Volatile
            private var instance: TaskRepository? = null

            fun getInstance(context: Context): TaskRepository {
                return instance ?: synchronized(this) {
                    if (instance == null) {
                        val database = TaskDatabase.getInstance(context)
                        instance = TaskRepository(database.taskDao())
                    }
                    instance as TaskRepository
                }
            }
        }
        //TODO 4 : Use FilterUtils.getFilteredQuery to create filterable query
        //TODO 5 : Build PagedList with configuration
        fun getTasks(filter: TasksFilterType): LiveData<PagedList<Task>> {
            val dataSourceFactory = when (filter) {
                TasksFilterType.ALL_TASKS -> tasksDao.getTasks()
                TasksFilterType.ACTIVE_TASKS -> tasksDao.getActiveTasks()
                TasksFilterType.COMPLETED_TASKS -> tasksDao.getCompletedTasks()
            }


            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(PLACEHOLDERS)
                .setPageSize(PAGE_SIZE)
                .build()

            return LivePagedListBuilder(dataSourceFactory, config).build()
        }







    fun getTaskById(taskId: Int): LiveData<Task> {
        return tasksDao.getTaskById(taskId)
    }

    fun getNearestActiveTask(): Task {
        return tasksDao.getNearestActiveTask()
    }

    suspend fun insertTask(newTask: Task): Long{
        return tasksDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        tasksDao.deleteTask(task.id)
    }

    suspend fun completeTask(task: Task, isCompleted: Boolean) {
        tasksDao.updateCompleted(task.id, isCompleted)
    }
}