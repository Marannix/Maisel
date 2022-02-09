package com.maisel.chat

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.message.usecase.GetMessagesUseCase
import com.maisel.domain.message.usecase.GetSenderUidUseCase
import com.maisel.domain.user.entity.SignUpUser
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

    fun setUser(user: SignUpUser) {
        viewState.value = currentViewState().copy(user = user)
    }

    fun startListeningToMessages(senderRoom: String) {
        messagesUseCase.startListeningToMessages(senderRoom)
    }

    fun stopListeningToMessages(senderRoom: String) {
        messagesUseCase.stopListeningToMessages(senderRoom)
    }

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
