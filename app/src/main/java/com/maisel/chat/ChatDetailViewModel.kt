package com.maisel.chat

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.UserInfo
import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import com.maisel.domain.user.usecase.GetRecipientUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userComposerController: UserComposerController,
    getRecipientUser: GetRecipientUserUseCase,
    private val messagesUseCase: GetMessagesUseCase,
    private val messageRepository: MessageRepository,
) : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    private val receiverId: String = checkNotNull(savedStateHandle["receiverId"])

    private val senderId = userComposerController.currentUser.value.userId ?: throw Exception()

    private val recipientUser: Flow<User> =
        getRecipientUser.invoke(receiverId).distinctUntilChanged()

    private fun currentViewState(): ChatDetailViewState = viewState.value!!


    fun init() {
        viewState.value = ChatDetailViewState(senderUid = senderId)
        listenToRecipientUser()
        getMessageItem(senderId, receiverId)
        listenToChatMessage(senderId, receiverId)
    }

    private fun listenToRecipientUser() {
        viewModelScope.launch(DispatcherProvider.Main) {
            viewState.value =
                currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Loading)

            recipientUser.collect { recipient ->
                viewState.value = currentViewState().copy(recipient = recipient)
            }
        }
    }

    private fun listenToChatMessage(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            viewState.value =
                currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Loading)
            messagesUseCase.invoke(senderId, receiverId)
                .collect { result ->
                    result.onSuccess { listOfMessages ->
                        Log.d("joshua message", listOfMessages.toString())
                        messageRepository.insertMessages(listOfMessages.toMutableStateList())
                    }
                    result.onFailure {
//                        viewState.value =
//                            currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Error)
                    }
                }
        }
    }

    private fun getMessageItem(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            messageRepository.getListOfChatMessages(senderId, receiverId).collect {
                if (it.isEmpty()) {
                    viewState.value =
                        currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Empty)
                } else {
                    viewState.value = currentViewState().copy(
                        messageItemState = GetMessagesUseCase.MessageDataState.Success(it)
                    )
                }
            }
        }
    }
}
