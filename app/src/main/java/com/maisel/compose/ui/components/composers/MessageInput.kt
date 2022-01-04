package com.maisel.compose.ui.components.composers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maisel.compose.state.messages.compose.MessageComposerState


/**
 * The default number of lines allowed in the input. The message input will become scrollable after
 * this threshold is exceeded.
 */
private const val DEFAULT_MESSAGE_INPUT_MAX_LINES = 6

/**
 * Input field for the Messages/Conversation screen. Allows label customization, as well as handlers
 * when the input changes.
 *
 * @param messageComposerState The state of the input.
 * @param onValueChange Handler when the value changes.
 * @param modifier Modifier for styling.
 * @param maxLines The number of lines that are allowed in the input.
 * @param label Composable function that represents the label UI, when there's no input/focus.
 */
@Composable
fun MessageInput(
    messageComposerState: MessageComposerState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = DEFAULT_MESSAGE_INPUT_MAX_LINES,
    label: @Composable () -> Unit = { DefaultComposerLabel() },
) {

    val (value) = messageComposerState

    InputField(
        modifier = modifier,
        value = value,
        maxLines = maxLines,
        onValueChange = onValueChange,
        decorationBox = { innerTextField ->
            Column {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    if (value.isEmpty()) {
                        label()
                    }

                    innerTextField()
                }
            }
        }
    )
}
