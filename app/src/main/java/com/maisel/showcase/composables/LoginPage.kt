package com.maisel.showcase.composables

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.showcase.composables.provider.ShowcaseProvider
import com.maisel.ui.orangish

//TODO: Support multi size
//https://proandroiddev.com/supporting-different-screen-sizes-on-android-with-jetpack-compose-f215c13081bd
// https://www.youtube.com/watch?v=iUIXsHiuRfY
//TODO: Can be deleted
@Preview(showBackground = true)
@Composable
fun LoginPage(@PreviewParameter(ShowcaseProvider::class) onLoginClicked: () -> Unit) {

    Box {
        BackgroundCard()
        MainCard(onLoginClicked)
    }

}

@Composable
fun BackgroundCard() {
    val signUpText = buildAnnotatedString {
        append("Don't have an account? ")
        withStyle(SpanStyle(color = orangish)) {
            append("Sign up!")
        }
    }

    //This is the background
    Surface(color = MaterialTheme.colors.primary, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-30).dp)) {
            Row() {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_fb), contentDescription = "Facebook icon" )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_google), contentDescription = "Google icon")
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = signUpText, color = Color.White)
        }
    }
}

@Composable
fun MainCard(onLoginClicked: () -> Unit) {

    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

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
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.padding(16.dp))
            CreateEmailAddressTextField(emailState, modifier)
            Spacer(modifier = Modifier.padding(6.dp))
            CreatePasswordTextField(passwordState, modifier)
            Spacer(modifier = Modifier.padding(vertical = 12.dp))

            //https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/samples/src/main/java/androidx/compose/material/samples/ContentAlphaSamples.kt
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text("Forgot Password?", textAlign = TextAlign.End, modifier = modifier)
            }
            Spacer(modifier = Modifier.padding(vertical = 12.dp))
            Button(
                onClick = { onLoginClicked.invoke() },
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(16.dp),

                modifier = modifier
            ) {
                Text(text = "Log In")
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
            Text(text = "Email")
        },
        placeholder = {
            Text(text = "Email")
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
