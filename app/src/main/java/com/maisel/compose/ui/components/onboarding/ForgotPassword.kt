package com.maisel.compose.ui.components.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.maisel.compose.ui.theme.typography

@Composable
fun ForgotPassword(label: String, onForgotPasswordClicked: () -> Unit, modifier: Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        Text(
            text = label,
            textAlign = TextAlign.End,
            modifier = modifier.clickable { onForgotPasswordClicked() },
            style = typography.subtitle2
        )
    }
}
