package com.maisel.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val sendMessageUseCase: SendMessageUseCase) : BaseViewModel() {

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
            if (it.senderRoom != null && it.senderUid != null && it.receiverRoom != null) {
                val model = MessageModel(it.senderUid, input, Date().time.toString())

                sendMessageUseCase.invoke(input, it.senderRoom, it.receiverRoom, model)
                state.value = currentViewState().copy(input = "")

            } else {
                Log.d("MessageViewModel: ", "Something has gone wrong")
            }
        }
    }

    fun setSenderUid(senderUid: String?) {
        if (senderUid != null) {
            state.value = currentViewState().copy(senderUid = senderUid)
        }
    }

    fun setSenderRoom(senderRoom: String?) {
        if (senderRoom != null) {
            state.value = currentViewState().copy(senderRoom = senderRoom)
        }
    }

    fun setReceiverRoom(receiverRoom: String?) {
        if (receiverRoom != null) {
            state.value = currentViewState().copy(receiverRoom = receiverRoom)
        }
    }

    fun onAttachmentRemoved() {
        //TODO:
    }

    fun onCameraClicked() {
        //TODO:
    }
}
