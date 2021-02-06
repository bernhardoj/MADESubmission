package com.example.madesubmission.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DayUtilTest {
    @Test
    fun testGetCurrentDay() {
        assertThat(DayUtil.getCurrentDay()).isGreaterThan(0)
    }
}