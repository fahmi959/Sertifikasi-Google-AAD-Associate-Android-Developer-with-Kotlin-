package com.fahmi.dicodingtodoaad.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteQuery

//TODO 2 : Define data access object (DAO)
@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM Task WHERE id = :taskId")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM Task WHERE isCompleted = 0 ORDER BY dueDateMillis ASC LIMIT 1")
    fun getNearestActiveTask(): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg tasks: Task)

    @Query("DELETE FROM Task WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT * FROM Task WHERE isCompleted = 0 ORDER BY dueDateMillis ASC")
    fun getActiveTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM Task WHERE isCompleted = 1 ORDER BY dueDateMillis ASC")
    fun getCompletedTasks(): DataSource.Factory<Int, Task>

    @Query("UPDATE Task SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
}

