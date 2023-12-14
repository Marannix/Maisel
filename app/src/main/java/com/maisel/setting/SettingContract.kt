package com.maisel.setting

import androidx.compose.runtime.Immutable
import com.maisel.common.base.UiEventBase
import com.maisel.common.base.UiStateBase
import com.maisel.common.base.UpdatedBaseViewModel
import com.maisel.domain.database.AppTheme

interface SettingContract {

    abstract class ViewModel : UpdatedBaseViewModel<UiEvents, UiState>()

    @Immutable
    data class UiState(
        val isLoading: Boolean,
        val appThemes: List<SettingThemeModel>,
        val currentAppTheme: AppTheme?,
        val isThemeDialogShown: Boolean,
    ) : UiStateBase

    sealed class UiEvents : UiEventBase {
        object ThemeClicked : UiEvents()
        object OnDialogDismissed : UiEvents()
    }
}
