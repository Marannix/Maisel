package com.maisel.compose.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.maisel.compose.ui.theme.ChatTheme

@Composable
fun OnboardingUserHeader(label: String, modifier: Modifier) {
    Text(
        text = label,
        style = ChatTheme.typography.h3,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}
