package com.example.madesubmission.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService

object KeyboardUtil {
    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager? =
            getSystemService(view.context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}