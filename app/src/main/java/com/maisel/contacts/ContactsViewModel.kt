package com.maisel.contacts

import androidx.lifecycle.viewModelScope
import com.maisel.dashboard.DashboardDestination
import com.maisel.dashboard.RecentMessageState
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
) : ContactsContract.ViewModel() {

    private val _destination = MutableSharedFlow<ContactsDestination>()
    val destination = _destination.asSharedFlow()

    override val _uiState: MutableStateFlow<ContactsContract.UiState> = MutableStateFlow(
        initialUiState()
    )

    init {
        getContacts()
    }
    override fun onUiEvent(event: ContactsContract.UiEvents) {
        when (event) {
            is ContactsContract.UiEvents.OnContactClicked -> {
                viewModelScope.launch {
                    if (event.contactId != null) {
                        _destination.emit(ContactsDestination.ChatDetail(event.contactId))
                    } else {
                        //TODO: Error screen
                    }
                }
            }
        }
    }

    private fun getContacts() {
        viewModelScope.launch {
            getUsersUseCase.invoke()
                .collectLatest { users ->
                    updateUiState { oldState ->
                        oldState.copy(
                            isLoading = false,
                            contacts = users
                        )
                    }
                }
        }
    }
    private fun initialUiState() = ContactsContract.UiState(
        isLoading = false,
        contacts = emptyList(),
    )
}
