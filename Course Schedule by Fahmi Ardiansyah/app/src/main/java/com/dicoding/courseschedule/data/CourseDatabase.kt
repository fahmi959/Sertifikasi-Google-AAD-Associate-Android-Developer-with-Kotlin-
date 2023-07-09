package com.dicoding.courseschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//TODO 3 : Define room database class

@Database(entities = [Course::class], version = 1)
abstract class CourseDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var instance: CourseDatabase? = null

        fun getInstance(context: Context): CourseDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CourseDatabase {
            return Room.databaseBuilder(context.applicationContext, CourseDatabase::class.java, "courses.db")
                .build()
        }
    }
}