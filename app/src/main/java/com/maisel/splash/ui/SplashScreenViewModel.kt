package com.maisel.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.database.usecase.LoadApplicationCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val loadApplicationCacheUseCase: LoadApplicationCacheUseCase,
    getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase
) : ViewModel() {

    val applicationCache = getApplicationCacheStateUseCase.invoke()

    init {
        viewModelScope.launch {
            loadApplicationCacheUseCase.invoke()
        }
    }
}
