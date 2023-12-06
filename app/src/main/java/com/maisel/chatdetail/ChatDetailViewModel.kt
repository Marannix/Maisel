package com.maisel.chatdetail

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.maisel.chatdetail.mapper.ChatDetailsMessageItemMapper
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.usecase.GetRecipientUserUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRecipientUser: GetRecipientUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val messagesUseCase: GetMessagesUseCase,
    private val messageRepository: MessageRepository,
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val messageMapper: ChatDetailsMessageItemMapper,
) : ChatDetailsContract.ViewModel() {

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val receiverId: String = checkNotNull(savedStateHandle["receiverId"])

    private val recipientUser: Flow<User> =
        getRecipientUser.invoke(receiverId).distinctUntilChanged()

    override val _uiState: MutableStateFlow<ChatDetailsContract.UiState> = MutableStateFlow(
        initialUiState()
    )

    fun init() {
        loadCache()
    }
//    fun init() {
//        viewState.value = ChatDetailViewState(senderUid = senderId)
//        listenToRecipientUser()
//        getMessageItem(senderId, receiverId)
//        listenToChatMessage(senderId, receiverId)
//    }

    private fun loadCache() {
        viewModelScope.launch {
            getApplicationCacheStateUseCase.invoke().collectLatest { cache ->
                when (cache) {
                    ApplicationCacheState.Error -> {
                        logOutUser()
                    }

                    is ApplicationCacheState.Loaded -> {
                        if (cache.settings.user != null) {
                            val senderId = (cache.settings.user as User).userId
                                ?: throw Exception()  //TODO: Create an exception (userId null)

                            updateUiState { oldState ->
                                oldState.copy(
                                    isLoading = true,
                                    senderUid = senderId
                                )
                            }

                            listenToRecipientUser()
                            getMessageItem(senderId, receiverId)
                            listenToChatMessage(senderId, receiverId)
                        } else {
                            viewModelScope.launch {
                                _snackbarMessage.emit("You are not logged in.")
                            }
                            logOutUser()
                        }
                    }

                    ApplicationCacheState.Loading -> {
                        // Create loading screen
                    }
                }
            }
        }
    }

    //TODO: Extract to base viewmodel and base activity
    private fun logOutUser() {
        viewModelScope.launch {
            logOutUseCase.invoke().collectLatest {
                //   updateUiState { oldState -> oldState.copy(userAuthState = UserAuthState.LOGGED_OUT) }
            }
        }
    }

    private fun listenToRecipientUser() {
        viewModelScope.launch(DispatcherProvider.Main) {

            recipientUser.collect { recipient ->
                updateUiState { oldState -> oldState.copy(recipient = recipient) }
            }
        }
    }

    private fun listenToChatMessage(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            messagesUseCase.invoke(senderId, receiverId)
                .collectLatest { result ->
                    result.onSuccess { listOfMessages ->
                        messageRepository.insertMessages(listOfMessages.toMutableStateList())
                    }
                    result.onFailure {
                        viewModelScope.launch {
                            _snackbarMessage.emit("Failed to load messages, please try again later.")
                        }
                    }
                }
        }
    }

    private fun getMessageItem(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            messageRepository.getListOfChatMessages(senderId, receiverId)
                .collectLatest { chatModel ->
                    if (chatModel.isEmpty()) {
                        updateUiState { oldState ->
                            oldState.copy(
                                isLoading = false,
                                messages = emptyList()
                            )
                        }
                    } else {
                        updateUiState { oldState ->
                            oldState.copy(
                                isLoading = false,
                                messages = messageMapper.invoke(chatModel, senderId)
                            )
                        }
                    }
                }
        }
    }


    override fun onUiEvent(event: ChatDetailsContract.UiEvents) {
        when (event) {
            is ChatDetailsContract.UiEvents.BackPressed -> {

            }

            ChatDetailsContract.UiEvents.CallClicked -> {
                viewModelScope.launch {
                    _snackbarMessage.emit("Call has not been implemented yet!")
                }
            }
            ChatDetailsContract.UiEvents.VideoClicked -> {
                viewModelScope.launch {
                    _snackbarMessage.emit("Video call has not been implemented yet!")
                }
            }
        }
    }

    private fun initialUiState() = ChatDetailsContract.UiState(
        isLoading = false,
        recipient = null,
        senderUid = null,
        error = null,
        messages = emptyList()
    )
}
