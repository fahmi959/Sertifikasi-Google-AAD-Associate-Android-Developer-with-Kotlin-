package com.dicoding.courseschedule.data

// TODO 1 : Define a local database table using the schema in app/schema/course.json
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "courseName") val courseName: String,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "startTime") val startTime: String,
    @ColumnInfo(name = "endTime") val endTime: String,
    @ColumnInfo(name = "lecturer") val lecturer: String,
    @ColumnInfo(name = "note") val note: String
)

