package com.maisel.showcase

import androidx.lifecycle.viewModelScope
import com.maisel.common.base.BaseViewModel
import com.maisel.domain.database.usecase.UpdateShowcaseStatusUseCase
import com.maisel.main.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(
    private val updateShowcaseStatusUseCase: UpdateShowcaseStatusUseCase
) : BaseViewModel() {

    fun setShowcase() {
        viewModelScope.launch {
            updateShowcaseStatusUseCase.invoke(hasSeenShowcase = true)
        }
    }
}
