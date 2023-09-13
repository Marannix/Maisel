package com.maisel.showcase

import androidx.lifecycle.viewModelScope
import com.maisel.common.base.BaseViewModel
import com.maisel.main.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
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
