package com.example.astrapokedex.screen.splashScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMFactorySplasScreen (private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSplashScreen::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelSplashScreen(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}