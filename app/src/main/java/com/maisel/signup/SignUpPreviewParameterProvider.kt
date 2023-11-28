package com.maisel.signup

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.maisel.common.composable.TextFieldState

internal class SignUpPreviewParameterProvider :
    PreviewParameterProvider<SignUpContract.UiState> {
    override val values = sequenceOf(
        SignUpContract.UiState(
            name = TextFieldState.Empty,
            email = TextFieldState.Empty,
            password = TextFieldState.Empty,
            errorMessage = "",
            isLoading = false,
            navigateToSignIn = false,
        )
    )
}
