package com.maisel.signin

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.maisel.common.composable.TextFieldState

internal class SignInPreviewParameterProvider :
    PreviewParameterProvider<SignInContract.SignInUiState> {
    override val values = sequenceOf(
        SignInContract.SignInUiState(
            email = TextFieldState.Empty,
            password = TextFieldState.Empty,
            errorMessage = "",
            isLoading = false
        )
    )
}
