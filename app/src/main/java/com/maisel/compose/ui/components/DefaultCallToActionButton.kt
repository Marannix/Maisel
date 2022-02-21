package com.maisel.compose.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun DefaultCallToActionButton(
    onClickListener: () -> Unit,
    label: String
) {
    Button(
        onClick = { onClickListener() },
        shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp)),
        contentPadding = PaddingValues(16.dp),
        elevation = ButtonDefaults.elevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        Text(text = label, textAlign = TextAlign.Center)
    }
}
