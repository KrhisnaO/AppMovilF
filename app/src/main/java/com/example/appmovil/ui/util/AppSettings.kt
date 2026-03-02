package com.example.appmovil.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppSettings {
    var fontScale by mutableFloatStateOf(1.0f)
        private set

    var darkMode by mutableStateOf(false)
        private set

    fun increaseFont() {
        fontScale = (fontScale + 0.1f).coerceAtMost(1.6f)
    }

    fun decreaseFont() {
        fontScale = (fontScale - 0.1f).coerceAtLeast(0.8f)
    }

    fun updateDarkMode(enabled: Boolean) {
        darkMode = enabled
    }
}