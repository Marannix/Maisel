package com.maisel.dashboard.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.ui.purplish

//TODO: Support multi size
//https://proandroiddev.com/supporting-different-screen-sizes-on-android-with-jetpack-compose-f215c13081bd
// https://www.youtube.com/watch?v=iUIXsHiuRfY
@Preview(showBackground = true)
@Composable
fun LoginPage1() {

    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

    Box {
        //This is the background
        Surface(color = purplish, modifier = Modifier.fillMaxSize()) {

        }

        //This is the white background section
        Surface(
            color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
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
                CreateEmailAddressTextField(emailState, modifier)
                Spacer(modifier = Modifier.padding(6.dp))
                CreatePasswordTextField(passwordState, modifier)
                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled ) {
                    Text("Forgot Password?", textAlign = TextAlign.End, modifier = modifier)
                }

            }
        }
    }

}

@Composable
private fun CreateEmailAddressTextField(
    emailState: MutableState<TextFieldValue>,
    modifier: Modifier
) {
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

@Composable
private fun CreatePasswordTextField(
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier
) {
    OutlinedTextField(
        value = passwordState.value, onValueChange = {
            passwordState.value = it
        },
        label = {
            Text(text = "Password")
        },
        placeholder = {
            Text(text = "Password")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Lock, contentDescription = "Password Icon")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier
    )
}