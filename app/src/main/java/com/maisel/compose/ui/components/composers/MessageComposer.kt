package com.maisel.compose.ui.components.composers

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maisel.R
import com.maisel.compose.state.messages.compose.MessageComposerState
import com.maisel.compose.ui.theme.ChatTheme
import com.maisel.message.MessageViewModel

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
    messageViewModel: MessageViewModel,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = { DefaultComposerLabel() },
    onValueChange: (String) -> Unit = { messageViewModel.setMessageInput(it) },
    onAttachmentsClick: () -> Unit = { },
    onCameraClick: () -> Unit = { },
    integrations: @Composable RowScope.(MessageComposerState) -> Unit = {
        DefaultComposerIntegrations(
            messageInputState = it,
            onAttachmentsClick = onAttachmentsClick,
            onCameraClick = onCameraClick
        )
    },
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
    val value: String =
        messageViewModel.state.observeAsState().value?.input ?: ""
    //    val cooldownTimer by viewModel.cooldownTimer.collectAsState()
    val cooldownTimer = 0

    MessageComposer(
        messageViewModel = messageViewModel,
        modifier = modifier,
        onSendMessage = { text ->
            messageViewModel.sendMessage(text)
//            val messageWithData = viewModel.buildNewMessage(text, attachments)
//
//            onSendMessage(messageWithData)
        },
        integrations = integrations,
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
fun MessageComposer(
    messageViewModel: MessageViewModel,
    messageComposerState: MessageComposerState,
    onSendMessage: (String) -> Unit = { messageViewModel.sendMessage(it) },
    modifier: Modifier = Modifier,
    shouldShowIntegrations: Boolean = true,
    integrations: @Composable RowScope.(MessageComposerState) -> Unit,
    input: @Composable RowScope.(MessageComposerState) -> Unit,
) {
    val (value, cooldownTimer) = messageComposerState

    Surface(
        modifier = modifier,
        elevation = 4.dp,
        color = ChatTheme.colors.barsBackground,
    ) {
        Column(Modifier.padding(vertical = 4.dp)) {
            //headerContent(messageComposerState)

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {

                if (shouldShowIntegrations) {
                    integrations(messageComposerState)
                } else {
                    Spacer(
                        modifier = Modifier.size(16.dp)
                    )
                }

                input(messageComposerState)

                if (cooldownTimer > 0) {
                 //   CooldownIndicator(cooldownTimer = cooldownTimer)
                } else {
                    val isInputValid = value.isNotEmpty()

                    IconButton(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        enabled = isInputValid,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = stringResource(id = R.string.compose_message_label),
                                tint = if (isInputValid) ChatTheme.colors.primaryAccent else  ChatTheme.colors.textLowEmphasis
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

/**
 * Composable that represents the message composer integrations (special actions).
 *
 * Currently just shows the Attachment picker action.
 *
 * @param messageInputState The state of the input.
 * @param onAttachmentsClick Handler when the user selects attachments.
 * @param onCameraClick Handler when the user selects camera.
 */
@Composable
internal fun DefaultComposerIntegrations(
    messageInputState: MessageComposerState,
    onAttachmentsClick: () -> Unit,
    onCameraClick: () -> Unit,
) {
    val hasTextInput = messageInputState.inputValue.isNotEmpty()
    val hasCommandInput = messageInputState.inputValue.startsWith("/")

    Row(
        modifier = Modifier
            .height(54.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = !hasCommandInput,
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp),
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attachments),
                    contentDescription = "",
                    tint = if (hasCommandInput) ChatTheme.colors.disabled else ChatTheme.colors.textLowEmphasis,
                )
            },
            onClick = onAttachmentsClick
        )

        IconButton(
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp),
            enabled = !hasTextInput,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null,
                )
            },
            onClick = onCameraClick
        )
    }
}

@Composable
internal fun DefaultComposerLabel() {
    Text(
        text = stringResource(id = R.string.compose_message_label),
        color = ChatTheme.colors.textLowEmphasis
    )
}
