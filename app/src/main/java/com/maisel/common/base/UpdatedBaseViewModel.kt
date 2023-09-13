package com.maisel.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class UpdatedBaseViewModel<E : UiEventBase, S : UiStateBase> : ViewModel() {

    abstract fun onUiEvent(event: E)

    protected open val _uiState: MutableStateFlow<S>
        get() = throw IllegalArgumentException("Not initialized")

    open val uiState: StateFlow<S> by lazy { _uiState }

    protected fun updateUiState(updateBlock: (oldState: S) -> S) {
        val newState = updateBlock(_uiState.value)
        _uiState.tryEmit(newState)
    }
}
