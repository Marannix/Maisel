package com.maisel.main

import androidx.lifecycle.viewModelScope
import com.maisel.common.base.BaseViewModel
import com.maisel.domain.database.AppTheme
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.database.usecase.LoadApplicationCacheUseCase
import com.maisel.utils.ResourceProvider
import com.maisel.utils.ThemeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val themeMapper: ThemeMapper
) : BaseViewModel() {

    val applicationCache = getApplicationCacheStateUseCase.invoke()

    fun getTheme(theme: String): AppTheme {
        return themeMapper.mapStringToEnum(theme)
    }

}
