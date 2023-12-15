package com.maisel.setting

import androidx.lifecycle.viewModelScope
import com.maisel.R
import com.maisel.domain.database.AppTheme
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.database.usecase.UpdateThemeUseCase
import com.maisel.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val resourceProvider: ResourceProvider,
    private val updateThemeUseCase: UpdateThemeUseCase,
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
                        updateUiState { oldState -> oldState.copy(isLoading = false) }

                        //TODO: Error screen
                    }

                    is ApplicationCacheState.Loaded -> {
                        val currentTheme = cache.settings.appTheme
                            ?: AppTheme.SYSTEM_DEFAULT

                        updateUiState { oldState ->
                            oldState.copy(
                                isLoading = false,
                                currentAppTheme = SettingThemeModel(mapThemeString(currentTheme), currentTheme),
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
                        updateUiState { oldState -> oldState.copy(isLoading = true) }
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

    private fun mapThemeString(appTheme: AppTheme): String {
        return when (appTheme) {
            AppTheme.LIGHT_MODE -> resourceProvider.getString(R.string.light)
            AppTheme.DARK_MODE ->  resourceProvider.getString(R.string.dark)
            AppTheme.SYSTEM_DEFAULT ->  resourceProvider.getString(R.string.system_default)
        }
    }

    private fun initialUiState() = SettingContract.UiState(
        isLoading = false,
        appThemes = emptyList(),
        isThemeDialogShown = false,
        themeOptions = emptyList()
    )
}
