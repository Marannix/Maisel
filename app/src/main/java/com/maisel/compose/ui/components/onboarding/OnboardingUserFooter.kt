package com.maisel.compose.ui.components.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.maisel.compose.ui.theme.ChatTheme

@Composable
fun OnboardingUserFooter(label: String, text: String, onClickListener: () -> Unit) {

    val spannedString = buildAnnotatedString {
        append(label)
        withStyle(SpanStyle(color = Color(0xFFF05777))) {
            append(text)
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = spannedString,
            style = ChatTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
                .clickable { onClickListener() }
        )
    }
}
