package com.maisel.showcase

import androidx.lifecycle.viewModelScope
import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import com.maisel.main.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : BaseViewModel() {

    fun setShowcase(newValue: Boolean) {
        viewModelScope.launch {
            appPreferences.setShowcase(newValue)
        }
    }
}
