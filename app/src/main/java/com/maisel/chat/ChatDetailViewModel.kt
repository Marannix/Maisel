package com.maisel.chat

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import com.maisel.domain.user.usecase.GetRecipientUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val messagesUseCase: GetMessagesUseCase,
    private val loggedInUser: GetLoggedInUserUseCase,
    private val messageRepository: MessageRepository,
    private val userComposerController: UserComposerController,
    private val getRecipientUser : GetRecipientUserUseCase
) : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    //private val currentUser: MutableStateFlow<User> = userComposerController.currentUser

    init {
        viewState.value = ChatDetailViewState(senderUid = loggedInUser.getLoggedInUser()!!.userId)
    }

    private fun currentViewState(): ChatDetailViewState = viewState.value!!

    fun setUser(userId: String) {
        val user = getRecipientUser.invoke(userId)
        viewState.value = currentViewState().copy(recipient = user)
    }

    fun listToChatMessage(senderId: String, receiverId: String) {
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

    fun getMessageItem(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            messageRepository.getListOfChatMessages(senderId, receiverId).collect {
                if (it.isNullOrEmpty()) {
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
