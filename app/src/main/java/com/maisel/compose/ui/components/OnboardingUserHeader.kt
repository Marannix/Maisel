package com.maisel.compose.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.maisel.compose.ui.theme.typography

@Composable
fun OnboardingUserHeader(label: String, modifier: Modifier) {
    Text(
        text = label,
        style = typography.h5,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}
