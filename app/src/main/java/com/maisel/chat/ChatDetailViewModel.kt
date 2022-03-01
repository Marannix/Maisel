package com.maisel.chat

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.message.usecase.GetSenderUidUseCase
import com.maisel.domain.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val messagesUseCase: GetMessagesUseCase,
    private val senderUidUseCase: GetSenderUidUseCase
) : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    init {
        viewState.value = ChatDetailViewState()
        getMessageItems()
    }

    private fun currentViewState(): ChatDetailViewState = viewState.value!!

    fun setUser(user: User) {
        viewState.value = currentViewState().copy(user = user)
    }

    fun startListeningToMessages(senderRoom: String, receiverId: String) {
        messagesUseCase.startListeningToMessages(senderRoom, receiverId)
    }

    fun stopListeningToMessages(senderRoom: String, receiverId: String) {
        messagesUseCase.stopListeningToMessages(senderRoom, receiverId)
    }

    //Current user is the sender
    fun getSenderUid(): String? {
        val senderUid = senderUidUseCase.invoke()
        viewState.value = currentViewState().copy(senderUid = senderUid)
        return senderUid
    }

    private fun getMessageItems() {
        messagesUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                viewState.value =
                    currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Loading)
            }
            .subscribe({
                viewState.value = currentViewState().copy(messageItemState = it)
            }, {
                viewState.value =
                    currentViewState().copy(messageItemState = GetMessagesUseCase.MessageDataState.Error)
            })
            .addDisposable()
    }
}
