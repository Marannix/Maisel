package com.maisel.chat

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maisel.common.BaseViewModel
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.usecase.GetLoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val messagesUseCase: GetMessagesUseCase,
    private val loggedInUser: GetLoggedInUser,
    private val messageRepository: MessageRepository
) : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    init {
        viewState.value = ChatDetailViewState(senderUid = loggedInUser.getLoggedInUser()!!.userId)
    }

    private fun currentViewState(): ChatDetailViewState = viewState.value!!

    fun setUser(user: User) {
        viewState.value = currentViewState().copy(user = user)
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
