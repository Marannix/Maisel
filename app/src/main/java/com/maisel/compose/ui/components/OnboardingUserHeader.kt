package com.maisel.compose.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OnboardingUserHeader(label: String, modifier: Modifier) {
    Text(
        text = label,
        style = MaterialTheme.typography.h3,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}
