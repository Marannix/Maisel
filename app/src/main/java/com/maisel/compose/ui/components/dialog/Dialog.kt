@file:OptIn(ExperimentalLayoutApi::class)

package com.maisel.compose.ui.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.maisel.compose.ui.theme.typography
import com.maisel.setting.SettingThemeModel

object Dialogs {

    /**
     * Alert dialog is a [Dialog] which interrupts the user with urgent information, details or actions.
     *
     * @param title The title to be shown as a header of the dialog
     * @param text The text which presents the details regarding the Dialog's purpose
     * @param dismissText The text to be shown for the dismiss button
     * @param onDismissRequest Executes when the user tries to dismiss the Dialog by clicking outside
     * or pressing the back button. This is not called when the dismiss button is clicked
     * @param modifier Modifier to be applied to the layout of the dialog.
     * @param confirmText The text to be shown for the confirm button. If this value is null, the
     * confirm button will not be displayed
     * @param onConfirmClick Executes when the user tries to click on the confirm button. If this
     * value is null, the confirm button will not be displayed
     * @param properties Typically platform specific properties to further configure the dialog.
     */
    @Composable
    fun AlertDialog(
        title: String,
        text: String,
        dismissText: String,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        confirmText: String? = null,
        onConfirmClick: (() -> Unit)? = null,
        properties: DialogProperties = DialogProperties(),
    ) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                if (confirmText != null && onConfirmClick != null) {
                    ConfirmButton(confirmText, onConfirmClick)
                }
            },
            modifier = Modifier
                .widthIn(280.dp, 560.dp)
                .then(modifier),
            dismissButton = { DismissButton(dismissText, onDismissRequest) },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                )
            },
            text = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.body2,
                )
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            properties = properties,
        )
    }

    @Composable
    private fun ConfirmButton(confirmText: String, onConfirmClick: (() -> Unit)) {
        TextButton(
            modifier = Modifier.padding(
                bottom = 8.dp,
                end = 8.dp
            ),
            onClick = onConfirmClick,
        ) {
            Text(
                text = confirmText,
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }

    @Composable
    private fun DismissButton(dismissText: String, onDismissRequest: () -> Unit) {
        TextButton(
            modifier = Modifier.padding(
                bottom = 8.dp,
                end = 8.dp,
            ),
            onClick = onDismissRequest,
        ) {
            Text(
                text = dismissText,
                style = MaterialTheme.typography.subtitle1,
            )
        }
    }

    @Composable
    fun ThemeAlertDialog(
        modifier: Modifier = Modifier,
        title: String,
        dismissText: String,
        onDismissRequest: () -> Unit,
        confirmText: String? = null,
        onConfirmClick: (() -> Unit)? = null,
        radioOptions: List<SettingThemeModel>,
        shape: Shape = MaterialTheme.shapes.medium,
        properties: DialogProperties = DialogProperties(),
        selectedOption: String,
        onOptionSelected: (SettingThemeModel) -> Unit
    ) {
       // val themeSelected = remember { mutableStateOf(currentTheme) }

        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            ThemeAlertContent(
                title = title,
                modifier = Modifier
                    .widthIn(280.dp, 560.dp)
                    .then(modifier),
                shape = shape,
                confirmButton = {
                    if (confirmText != null && onConfirmClick != null) {
                        ConfirmButton(confirmText, onConfirmClick)
                    }
                },
                dismissButton = { DismissButton(dismissText, onDismissRequest) },
                radioOptions = radioOptions,
                selectedOption = selectedOption,
                onOptionSelected = onOptionSelected
            )
        }
    }

    @Composable
    private fun ThemeAlertContent(
        modifier: Modifier = Modifier,
        title: String,
        confirmButton: @Composable () -> Unit,
        dismissButton: @Composable (() -> Unit)? = null,
        radioOptions: List<SettingThemeModel>,
        selectedOption: String,
        onOptionSelected: (SettingThemeModel) -> Unit,
        shape: Shape,
    ) {
//        val radioOptions = listOf(
//            stringResource(id = R.string.system_default),
//            stringResource(id = R.string.light),
//            stringResource(id = R.string.dark)
//        )


        Surface(
            modifier = modifier,
            shape = shape,
        ) {
            Column(
                Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = title,
                    style = typography.h5,
                )
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text.name == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            )
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text.name == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            modifier = modifier.padding(horizontal = 16.dp),
                            text = text.name
                        )
                    }
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                ) {
                    FlowRow(
                        modifier = Modifier.align(Alignment.CenterEnd)
//                        mainAxisSpacing = 8.dp,
//                        crossAxisSpacing = 12.dp
                    ) {
                        dismissButton?.invoke()
                        confirmButton()
                    }
                }
            }

        }
    }
}
