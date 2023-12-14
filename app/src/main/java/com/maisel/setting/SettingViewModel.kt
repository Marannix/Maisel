package com.maisel.setting

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import com.maisel.R
import com.maisel.domain.database.AppTheme
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.database.usecase.UpdateThemeUseCase
import com.maisel.utils.ResourceProvider
import com.maisel.utils.ThemeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val resourceProvider: ResourceProvider,
    private val updateThemeUseCase: UpdateThemeUseCase
) : SettingContract.ViewModel() {

    override val _uiState = MutableStateFlow(
        initialUiState()
    )

    init {
        loadCache()
    }

    private fun loadCache() {
        viewModelScope.launch {
            getApplicationCacheStateUseCase.invoke().collectLatest { cache ->
                when (cache) {
                    ApplicationCacheState.Error -> {
                        //TODO: Error screen
                    }

                    is ApplicationCacheState.Loaded -> {
                        updateUiState { oldState ->
                            oldState.copy(
                                currentAppTheme = cache.settings.appTheme
                                    ?: AppTheme.SYSTEM_DEFAULT,
                                appThemes = listOf(
                                    SettingThemeModel(
                                        resourceProvider.getString(R.string.system_default),
                                        AppTheme.SYSTEM_DEFAULT
                                    ),
                                    SettingThemeModel(
                                        resourceProvider.getString(R.string.light),
                                        AppTheme.LIGHT_MODE
                                    ),
                                    SettingThemeModel(
                                        resourceProvider.getString(R.string.dark),
                                        AppTheme.DARK_MODE
                                    )
                                )
                            )
                        }
                    }

                    ApplicationCacheState.Loading -> {
                        // Create loading screen
                    }
                }
            }
        }
    }

    override fun onUiEvent(event: SettingContract.UiEvents) {
        when (event) {
            SettingContract.UiEvents.ThemeClicked -> {
                updateUiState { oldState -> oldState.copy(isThemeDialogShown = true) }
            }

            SettingContract.UiEvents.OnDialogDismissed -> {
                updateUiState { oldState -> oldState.copy(isThemeDialogShown = false) }
            }

            is SettingContract.UiEvents.OnDialogConfirmed -> {
                updateUiState { oldState -> oldState.copy(isThemeDialogShown = false) }
                viewModelScope.launch {
                    updateThemeUseCase.invoke(appTheme = event.appTheme)
                }
            }
        }
    }

    private fun initialUiState() = SettingContract.UiState(
        isLoading = false,
        appThemes = emptyList(),
//        currentAppTheme = null,
        isThemeDialogShown = false
    )
}
