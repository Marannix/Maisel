package com.maisel.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.usecase.SendMessageUseCase
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val loggedInUser: GetLoggedInUserUseCase
) :
    BaseViewModel() {

    val state = MutableLiveData<MessageState>()

    init {
        state.value = MessageState()
    }

    private fun currentViewState(): MessageState = state.value!!

    fun setMessageInput(input: String) {
        state.value = currentViewState().copy(input = input)
    }

    fun sendMessage(input: String) {
        state.value?.let {
            if (it.senderUid != null && it.receiverId != null) {
                val model =
                    ChatDataModel(it.senderUid, it.receiverId, input, Date().time.toString())

                sendMessageUseCase.invoke(input, it.senderUid, it.receiverId, model)
                state.value = currentViewState().copy(input = "")

            } else {
                Log.d("MessageViewModel: ", "Something has gone wrong")
            }
        }
    }

    fun setSenderUid(senderUid: String?) {
        state.value = currentViewState().copy(senderUid = senderUid)
    }

    fun setReceiverId(receiverId: String?) {
        state.value = currentViewState().copy(receiverId = receiverId)
    }

    fun onAttachmentRemoved() {
        //TODO:
    }

    fun onCameraClicked() {
        //TODO:
    }
}
