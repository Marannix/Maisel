package com.maisel.compose.state.messages.compose

import com.maisel.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class MessageComposerController {

    /**
     * Creates a [CoroutineScope] that allows us to cancel the ongoing work when the parent
     * ViewModel is disposed.
     */
    private val scope = CoroutineScope(DispatcherProvider.Main)

    /**
     * UI state of the current composer input.
     */
    val input: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Gets the current text input in the message composer.
     */
    private val messageText: String
        get() = input.value
}
