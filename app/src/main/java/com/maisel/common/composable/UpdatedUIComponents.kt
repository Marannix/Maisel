package com.maisel.common.composable

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.common.composable.UiComponentTestTag.EmailTag
import com.maisel.common.composable.UiComponentTestTag.NameTag
import com.maisel.common.composable.UiComponentTestTag.PasswordTag
import com.maisel.compose.ui.theme.typography

@ExperimentalComposeUiApi
@Composable
fun PasswordContent(
    modifier: Modifier = Modifier,
    passwordState: TextFieldState,
    onValueChange: (String) -> Unit
) {

    val isError = passwordState.isInvalid()
    val errorMessage = passwordState.errorMessageOrNull()
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.testTag(PasswordTag),
        value = passwordState.text,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = R.string.password), style = typography.body2)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.password), style = typography.body2)
        },
        visualTransformation = setPasswordVisualTransformation(showPassword),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions = KeyboardActions.Default,
        trailingIcon = { SetPasswordTrailingIcon(showPassword) },
        singleLine = true,
        maxLines = 1
    )

    AnimatedVisibility(visible = isError) {
        if (isError && errorMessage != null) {
            TextFieldErrorText(errorMessage)
        }
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
fun EmailContent(
    modifier: Modifier = Modifier,
    emailState: TextFieldState,
    onValueChange: (String) -> Unit
) {

    val isError = emailState.isInvalid()
    val errorMessage = emailState.errorMessageOrNull()

    OutlinedTextField(
        modifier = modifier.testTag(EmailTag),
        value = emailState.text,
        onValueChange = onValueChange,
        label = {
            Text(text = "Email", style = typography.body2)
        },
        placeholder = {
            Text(text = "Email", style = typography.body2)
        },
        isError = emailState.isInvalid(),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
        maxLines = 1,
    )

    AnimatedVisibility(visible = isError) {
        if (isError && errorMessage != null) {
            TextFieldErrorText(errorMessage)
        }
    }
}

@Composable
fun UpdatedDefaultNameContent(
    modifier: Modifier = Modifier,
    nameState: TextFieldState,
    onValueChange: (String) -> Unit
) {

    val isError = nameState.isInvalid()
    val errorMessage = nameState.errorMessageOrNull()

    OutlinedTextField(
        modifier = modifier.testTag(NameTag),
        value = nameState.text,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(text = "Name", style = typography.body2)
        },
        placeholder = {
            Text(text = "Name", style = typography.body2)
        },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions.Default,
        maxLines = 1
    )

    AnimatedVisibility(visible = isError) {
        if (isError && errorMessage != null) {
            TextFieldErrorText(errorMessage)
        }
    }
}

@Composable
private fun TextFieldErrorText(errorMessage: String, modifier: Modifier = Modifier) {
    Text(
        text = errorMessage,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colors.error,
        style = typography.caption,
        modifier = modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
    )
}

object UiComponentTestTag {
    const val EmailTag = "EmailAddress"
    const val NameTag = "Name"
    const val PasswordTag = "Password"
}
