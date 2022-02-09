package com.maisel.message

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor() : BaseViewModel() {

    val state = MutableLiveData<MessageState>()

    init {
        state.value = MessageState()
    }

    private fun currentViewState(): MessageState = state.value!!

    fun setMessageInput(input: String) {
        state.value = currentViewState().copy(input = input)
    }

    fun sendMessage() {

    }



    fun onAttachmentRemoved() {
        //TODO:
    }

    fun onCameraClicked() {
        //TODO:
    }
}
