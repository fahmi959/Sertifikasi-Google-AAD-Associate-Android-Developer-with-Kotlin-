package com.dicoding.courseschedule.util

import java.util.*

enum class DayName(val value: String) {
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");


    companion object {
        fun getByNumber(dayNumber: Int) = when (dayNumber) {
            Calendar.SUNDAY -> SUNDAY.value
            Calendar.MONDAY -> MONDAY.value
            Calendar.TUESDAY -> TUESDAY.value
            Calendar.WEDNESDAY -> WEDNESDAY.value
            Calendar.THURSDAY -> THURSDAY.value
            Calendar.FRIDAY -> FRIDAY.value
            Calendar.SATURDAY -> SATURDAY.value

              else -> MONDAY.value
        }
    }
}