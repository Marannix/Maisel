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
        val currentAppTheme: SettingThemeModel = SettingThemeModel("System default", AppTheme.SYSTEM_DEFAULT),
        val isThemeDialogShown: Boolean,
        val themeOptions: List<Pair<String, AppTheme>>
    ) : UiStateBase

    sealed class UiEvents : UiEventBase {
        object ThemeClicked : UiEvents()
        data class OnDialogConfirmed(val appTheme: AppTheme): UiEvents()
        object OnDialogDismissed : UiEvents()
    }
}
