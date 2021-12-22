package com.maisel.common.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R


@ExperimentalComposeUiApi
@Composable
fun CreatePasswordTextField(
    passwordState: MutableState<TextFieldValue>,
    showPasswordError: Boolean = false,
    modifier: Modifier,
    focusRequester: FocusRequester
) {
    val showPassword = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester).testTag("password"),
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        trailingIcon = { SetPasswordTrailingIcon(showPassword) },
        singleLine = true
    )

    if (showPasswordError) {
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
fun CreateEmailAddressTextField(
    emailState: MutableState<TextFieldValue>,
    showEmailError: Boolean,
    modifier: Modifier,
    focusRequester: FocusRequester
) {

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester).testTag("email"),
        value = emailState.value, onValueChange = {
            emailState.value = it
        },
        label = {
            Text(text = "Email")
        },
        placeholder = {
            Text(text = "Email")
        },
        isError = showEmailError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() }
        )
    )
    if (showEmailError) {
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
fun CreateNameTextField(
    nameState: MutableState<TextFieldValue>,
    showNameError: Boolean,
    modifier: Modifier,
    focusRequester: FocusRequester
) {
    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester).testTag("name"),
        value = nameState.value, onValueChange = {
            nameState.value = it
        },
        label = {
            Text(text = "Name")
        },
        placeholder = {
            Text(text = "Name")
        },
        isError = showNameError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusRequester.requestFocus() }
        )
    )
    if (showNameError) {
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
