package com.maisel.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maisel.common.BaseViewModel
import com.maisel.coroutine.DispatcherProvider
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
    private val loggedInUser: GetLoggedInUser
) : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    init {
        viewState.value = ChatDetailViewState(senderUid = loggedInUser.getLoggedInUser()!!.userId)
    }

    private fun currentViewState(): ChatDetailViewState = viewState.value!!

    fun setUser(user: User) {
        viewState.value = currentViewState().copy(user = user)
    }

    fun getMessageItems(senderId: String, receiverId: String) {
        viewModelScope.launch(DispatcherProvider.Main) {
            viewState.value =
                currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Loading)
            messagesUseCase.invoke(senderId, receiverId)
                .collect { result ->
                    result.onSuccess {
                        if (it.isNullOrEmpty()) {
                            viewState.value =
                                currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Empty)
                        } else {
                            viewState.value = currentViewState().copy(
                                messageItemState = GetMessagesUseCase.MessageDataState.Success(it)
                            )
                        }
                    }
                    result.onFailure {
                        viewState.value =
                            currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Error)
                    }
                }
        }
    }
}
