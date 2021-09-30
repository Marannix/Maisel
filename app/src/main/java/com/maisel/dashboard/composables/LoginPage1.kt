package com.maisel.dashboard.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.ui.purplish

@Preview(showBackground = true)
@Composable
fun LoginPage1() {
    val emailAddress = ""
    val emailState = remember { mutableStateOf(TextFieldValue(emailAddress)) }

    Box {
        //This is the background
        Surface(color = purplish, modifier = Modifier.fillMaxSize()) {

        }

        //This is the white background section
        Surface(
            color = Color.White, modifier = Modifier
                .height(600.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(60.dp).copy(
                topStart = ZeroCornerSize,
                topEnd = ZeroCornerSize
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                Image(
                    ImageVector.vectorResource(id = R.drawable.ic_undraw_chatting),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.padding(16.dp))
                OutlinedTextField(
                    value = emailState.value, onValueChange = {
                        emailState.value = it
                    },
                    label = {
                        Text(text = "Email address")
                    },
                    placeholder = {
                        Text(text = "Email address")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Email, contentDescription = "Email Icon")
                    },
                    modifier = modifier
                )
            }
        }
    }
}