package com.maisel.common.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.state.ValidationError
import com.maisel.compose.state.onboarding.compose.AuthenticationState

@ExperimentalComposeUiApi
@Composable
fun DefaultPasswordContent(
    state: ValidationError.AuthenticationError,
    value: AuthenticationState,
    onValueChange: (AuthenticationState) -> Unit,
    modifier: Modifier,
    onImeAction: () -> Unit
) {
    var passwordFieldValueState by remember { mutableStateOf(TextFieldValue(text = value.password)) }

    val showPassword = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier.testTag("password"),
        value = passwordFieldValueState, onValueChange = {
            passwordFieldValueState = it
            if (value.password != it.text) {
                onValueChange(AuthenticationState(value.name, value.email, it.text))
            }
        },
        label = {
            Text(text = stringResource(id = R.string.password))
        },
        placeholder = {
            Text(text = stringResource(id = R.string.password))
        },
        visualTransformation = setPasswordVisualTransformation(showPassword),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onImeAction()
            }
        ),
        trailingIcon = { SetPasswordTrailingIcon(showPassword) },
        singleLine = true
    )

    if (state.passwordError) {
        Text(
            text = "Password must be 8 characters long",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
    }
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

@Composable
fun DefaultEmailContent(
    state: ValidationError.AuthenticationError,
    value: AuthenticationState,
    onValueChange: (AuthenticationState) -> Unit,
    modifier: Modifier,
    onImeAction: () -> Unit,
) {
    var emailFieldValueState by remember { mutableStateOf(TextFieldValue(text = value.email)) }

    OutlinedTextField(
        modifier = modifier.testTag("email"),
        value = emailFieldValueState, onValueChange = {
            emailFieldValueState = it
            if (value.email != it.text) {
                onValueChange(AuthenticationState(value.name, it.text, value.password))
            }
        },
        label = {
            Text(text = "Email")
        },
        placeholder = {
            Text(text = "Email")
        },
        isError = state.emailError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() }
        )
    )
    if (state.emailError) {
        Text(
            text = "Please enter a valid email",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun DefaultNameContent(
    modifier: Modifier,
    state: ValidationError.AuthenticationError,
    value: AuthenticationState,
    onValueChange: (AuthenticationState) -> Unit,
    onImeAction: () -> Unit,
) {

    var nameFieldValueState by remember { mutableStateOf(TextFieldValue(text = value.name)) }

    OutlinedTextField(
        modifier = modifier.testTag("name"),
        value = nameFieldValueState, onValueChange = {
            nameFieldValueState = it
            if (value.password != it.text) {
                onValueChange(AuthenticationState(it.text, value.email, value.password))
            }
        },
        label = {
            Text(text = "Name")
        },
        placeholder = {
            Text(text = "Name")
        },
        isError = state.nameError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() }
        )
    )
    if (state.nameError) {
        Text(
            text = "Please enter a valid name",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
        )
    }
}
