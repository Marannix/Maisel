package com.maisel.compose.ui.components.composers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maisel.compose.ui.theme.extendedColors
import com.maisel.compose.ui.theme.typography
import com.maisel.ui.shapes


/**
 * Custom input field that we use for our UI. It's fairly simple - shows a basic input with clipped
 * corners and a border stroke, with some extra padding on each side.
 *
 * Within it, we allow for custom decoration, so that the user can define what the input field looks like
 * when filled with content.
 *
 * @param value The current input value.
 * @param onValueChange Handler when the value changes as the user types.
 * @param modifier Modifier for styling.
 * @param maxLines The number of lines that are allowed in the input, no limit by default.
 * @param border The [BorderStroke] that will appear around the input field.
 * @param innerPadding The padding inside the input field, around the label or input.
 * @param decorationBox Composable function that represents the input field decoration as it's filled with content.
 */
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.extendedColors.borders),
    innerPadding: Dp = 8.dp,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }

    // Workaround to move cursor to the end after selecting a suggestion
    val selection = if (textFieldValueState.isCursorAtTheEnd()) {
        TextRange(value.length)
    } else {
        textFieldValueState.selection
    }

    val textFieldValue = textFieldValueState.copy(
        text = value,
        selection = selection
    )

    BasicTextField(
        modifier = modifier
            // .border(border = border, shape = ChatTheme.shapes.inputField)
            .border(border = border, shape = RoundedCornerShape(24.dp))
            .clip(shapes.medium.copy(CornerSize(24.dp)))
            .background(MaterialTheme.extendedColors.inputBackground)
            .padding(innerPadding),
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        textStyle = typography.body1.copy(
            color = MaterialTheme.extendedColors.highEmphasis,
        ),
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        decorationBox = { innerTextField -> decorationBox(innerTextField) },
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}

/**
 * Check if the [TextFieldValue] state represents a UI with the cursor at the end of the input.
 *
 * @return True if the cursor is at the end of the input.
 */
private fun TextFieldValue.isCursorAtTheEnd(): Boolean {
    val textLength = text.length
    val selectionStart = selection.start
    val selectionEnd = selection.end

    return textLength == selectionStart && textLength == selectionEnd
}
