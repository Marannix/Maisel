package com.maisel.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.maisel.common.base.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userComposerController: UserComposerController,
    private val sendMessageUseCase: SendMessageUseCase
) : BaseViewModel() {

    //TODO: Convert all live data to flow
    val state = MutableLiveData<MessageState>()

    private val receiverId: String = checkNotNull(savedStateHandle["receiverId"])
    private val senderId = userComposerController.currentUser.value.userId ?: throw Exception()

    fun init() {
        state.value = MessageState(receiverId = receiverId, senderUid = senderId)
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
}
