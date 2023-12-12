package com.maisel.setting

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : SettingContract.ViewModel() {

    override val _uiState = MutableStateFlow(
        initialUiState()
    )

    private fun initialUiState() = SettingContract.UiState

    override fun onUiEvent(event: SettingContract.UiEvents) {

    }
}