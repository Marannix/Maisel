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
            if (it.senderUid != null && it.receiverId != null) {
                //TODO: Comeback here later, shouldn't need to set userId
                val model = MessageModel("", it.senderUid, it.receiverId, input, Date().time.toString())

                sendMessageUseCase.invoke(input, it.senderUid, it.receiverId, model)
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

//    fun setSenderRoom(senderRoom: String?) {
//        if (senderRoom != null) {
//            state.value = currentViewState().copy(senderRoom = senderRoom)
//        }
//    }
//
//    fun setReceiverRoom(receiverRoom: String?) {
//        if (receiverRoom != null) {
//            state.value = currentViewState().copy(receiverRoom = receiverRoom)
//        }
//    }

    fun setReceiverId(receiverId: String?) {
        if (receiverId != null) {
            state.value = currentViewState().copy(receiverId = receiverId)
        }
    }

    fun onAttachmentRemoved() {
        //TODO:
    }

    fun onCameraClicked() {
        //TODO:
    }
}
