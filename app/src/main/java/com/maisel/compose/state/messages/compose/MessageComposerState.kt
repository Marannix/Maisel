package com.maisel.compose.state.messages.compose

/**
 * Represents the state within the message input.
 *
 * @param inputValue The current text value that's within the input.
 * @param cooldownTimer The amount of time left until the user is allowed to send the next message.
 */
data class MessageComposerState(
    val inputValue: String = "",
    val cooldownTimer: Int = 0,
)
