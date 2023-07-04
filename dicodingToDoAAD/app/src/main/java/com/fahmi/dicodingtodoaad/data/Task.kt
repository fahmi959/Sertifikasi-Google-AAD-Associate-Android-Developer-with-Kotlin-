package com.fahmi.dicodingtodoaad.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO 1 : Define a local database table using the schema in app/schema/tasks.json

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "dueDateMillis")
    val dueDateMillis: Long,

    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false
)