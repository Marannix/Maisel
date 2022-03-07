package com.maisel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.maisel.coroutine.DispatcherProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope

@HiltAndroidApp
class MainApplication : Application() {

    private val scope = CoroutineScope(DispatcherProvider.Main)

    init {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
