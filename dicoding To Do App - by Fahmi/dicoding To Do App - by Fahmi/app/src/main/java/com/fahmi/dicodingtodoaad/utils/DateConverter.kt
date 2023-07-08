package com.fahmi.dicodingtodoaad.utils

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    fun convertMillisToString(timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }


    fun isPastDeadline(dueDateMillis: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return dueDateMillis < currentTimeMillis
    }
}