package com.maisel.splash.ui

import androidx.lifecycle.ViewModel
import com.maisel.domain.database.AppTheme
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.utils.ThemeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val themeMapper: ThemeMapper
) : ViewModel() {

    val applicationCache = getApplicationCacheStateUseCase.invoke()

    fun mapThemeToString(theme: AppTheme?): String? {
        return themeMapper.mapEnumToString(theme)
    }
}
