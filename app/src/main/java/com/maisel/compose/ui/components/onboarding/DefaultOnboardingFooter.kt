package com.maisel.compose.ui.components.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R

@Composable
fun DefaultOnboardingFooter(
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit,
    label: String) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.padding(vertical = 20.dp))
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_fb),
                contentDescription = "Facebook icon",
                modifier = Modifier.clickable { onFacebookClicked() }
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_google),
                contentDescription = "Google icon",
                modifier = Modifier.clickable { onGoogleClicked() },
            )
        }
    }
}
