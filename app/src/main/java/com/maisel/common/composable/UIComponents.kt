package com.maisel.common.composable

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import com.maisel.R


@Composable
fun CreatePasswordTextField(
    passwordState: MutableState<TextFieldValue>,
    modifier: Modifier
) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = passwordState.value, onValueChange = {
            passwordState.value = it
        },
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        placeholder = {
            Text(text = stringResource(id = R.string.password))
        },
        visualTransformation = setPasswordVisualTransformation(showPassword),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = { SetPasswordTrailingIcon(showPassword) },
        singleLine = true
    )
}

@Composable
private fun SetPasswordTrailingIcon(showPassword: MutableState<Boolean>) {
    if (showPassword.value) {
        IconButton(onClick = { showPassword.value = false }) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = stringResource(id = R.string.hide_password)
            )
        }
    } else {
        IconButton(onClick = { showPassword.value = true }) {
            Icon(
                imageVector = Icons.Filled.VisibilityOff,
                contentDescription = stringResource(id = R.string.show_password)
            )
        }
    }
}

@Composable
private fun setPasswordVisualTransformation(showPassword: MutableState<Boolean>) =
    if (showPassword.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }