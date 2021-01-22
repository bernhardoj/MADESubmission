package com.example.madesubmission.core.utils

import java.util.*

object DayUtil {
    fun getCurrentDay() = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DAY_OF_YEAR)
}