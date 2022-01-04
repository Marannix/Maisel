package com.maisel.compose.ui.components.composers

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.compose.state.messages.compose.MessageComposerState

/**
 * Default MessageComposer component that relies on [MessageComposerViewModel] to handle data and
 * communicate various events.
 *
 * @param viewModel The ViewModel that provides pieces of data to show in the composer, like the
 * currently selected integration data or the user input. It also handles sending messages.
 * @param modifier Modifier for styling.
 * @param onSendMessage Handler when the user sends a message. By default it delegates this to the
 * ViewModel, but the user can override if they want more custom behavior.
 * @param onValueChange Handler when the input field value changes.
 * @param onAttachmentRemoved Handler when the user taps on the cancel/delete attachment action.
 * @param onCancelAction Handler for the cancel button on Message actions, such as Edit and Reply.
 * @param integrations A view that represents custom integrations. By default, we provide
 * [DefaultComposerIntegrations], which show Attachments & Giphy, but users can override this with
 * their own integrations, which they need to hook up to their own data providers and UI.
 * @param label The input field label (hint).
 * @param input The input field for the composer, [MessageInput] by default.
 */
@Composable
fun MessageComposer(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = { DefaultComposerLabel() },
    onValueChange: (String) -> Unit = { },
    input: @Composable RowScope.(MessageComposerState) -> Unit = {
        MessageInput(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .weight(1f),
            label = label,
            messageComposerState = it,
            onValueChange = onValueChange
        )
    },
) {
  //  val value by viewModel.input.collectAsState()
    //    val cooldownTimer by viewModel.cooldownTimer.collectAsState()
    val value = ""
    val cooldownTimer = 0

    MessageComposer(
        modifier = modifier,
        onSendMessage = { text ->
//            val messageWithData = viewModel.buildNewMessage(text, attachments)
//
//            onSendMessage(messageWithData)
        },
        input = input,
        messageComposerState = MessageComposerState(
            inputValue = value,
            cooldownTimer = cooldownTimer
        )
    )
}

/**
 * Clean version of the [MessageComposer] that doesn't rely on ViewModels, so the user can provide a
 * manual way to handle and represent data and various operations.
 *
 * @param messageComposerState The state of the message input.
 * @param onSendMessage Handler when the user taps on the send message button.
 * @param onCancelAction Handler when the user cancels the active action (Reply or Edit).
 * @param onMentionSelected Handler when the user taps on a mention suggestion item.
 * @param onCommandSelected Handler when the user taps on a command suggestion item.
 * @param onAlsoSendToChannelSelected Handler when the user checks the also send to channel checkbox.
 * @param modifier Modifier for styling.
 * @param headerContent The content shown at the top of the message composer.
 * @param footerContent The content shown at the bottom of the message composer.
 * @param mentionPopupContent Customizable composable function that represents the mention suggestions popup.
 * @param commandPopupContent Customizable composable function that represents the instant command suggestions popup.
 * @param shouldShowIntegrations If we should show or hide integrations.
 * @param integrations Composable that represents integrations for the composer, such as Attachments.
 * @param input Composable that represents the input field in the composer.
 */
@Composable
public fun MessageComposer(
    messageComposerState: MessageComposerState,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
    input: @Composable RowScope.(MessageComposerState) -> Unit,
) {
    val (value, cooldownTimer) = messageComposerState

    Surface(
        modifier = modifier,
        elevation = 4.dp,
       // color = ChatTheme.colors.barsBackground,
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            //headerContent(messageComposerState)

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {

                input(messageComposerState)

                if (cooldownTimer > 0) {
                 //   CooldownIndicator(cooldownTimer = cooldownTimer)
                } else {
                    val isInputValid = value.isNotEmpty()

                    IconButton(
                        enabled = isInputValid,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dough),
                                contentDescription = stringResource(id = R.string.compose_message_label),
                                tint = if (isInputValid) MaterialTheme.colors.primary else  MaterialTheme.colors.secondary
                            )
                        },
                        onClick = {
                            if (isInputValid) {
                                onSendMessage(value)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun DefaultComposerLabel() {
    Text(
        text = stringResource(id = R.string.compose_message_label)
       // , color = ChatTheme.colors.textLowEmphasis
    )
}
