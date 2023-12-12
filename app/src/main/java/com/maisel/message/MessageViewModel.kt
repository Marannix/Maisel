package com.maisel.message

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.message.ChatDataModel
import com.maisel.domain.message.usecase.SendMessageUseCase
import com.maisel.domain.user.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
) : MessageContract.ViewModel() {

    private val receiverId: String = checkNotNull(savedStateHandle["receiverId"])

    override val _uiState: MutableStateFlow<MessageContract.UiState> = MutableStateFlow(
        initialUiState()
    )

    fun init() {
        loadCache()
    }

    private fun loadCache() {
        viewModelScope.launch {
            getApplicationCacheStateUseCase.invoke().collectLatest { cache ->
                when (cache) {
                    ApplicationCacheState.Error -> {
                        //NO-OP
                    }

                    is ApplicationCacheState.Loaded -> {
                        if (cache.settings.user != null) {
                            val senderId = (cache.settings.user as User).userId
                                ?: throw Exception()
                            //TODO: Create an exception (userId null)
                            //TODO: Exit screen

                            updateUiState { oldState ->
                                oldState.copy(
                                    receiverId = receiverId,
                                    senderUid = senderId
                                )
                            }
                        }
                    }

                    ApplicationCacheState.Loading -> {
                        //NO-OP
                    }
                }
            }
        }
    }

    private fun setMessageInput(input: String) {
        viewModelScope.launch {
            updateUiState { oldState -> oldState.copy(input = input) }
        }
    }

    private fun sendMessage(input: String) {
        val senderUid = uiState.value.senderUid
        val receiverId = uiState.value.receiverId

        if (senderUid != null && receiverId != null) {
            val model = ChatDataModel(senderUid, receiverId, input, Date().time.toString())

            sendMessageUseCase.invoke(input, senderUid, receiverId, model)
            updateUiState { oldState -> oldState.copy(input = "") }
        } else {
            Log.d("MessageViewModel: ", "Message failed to send")
        }
    }

    override fun onUiEvent(event: MessageContract.UiEvents) {
        when (event) {
            is MessageContract.UiEvents.SendMessage -> {
                sendMessage(event.message)
            }

            is MessageContract.UiEvents.MessageUpdated -> {
                setMessageInput(event.message)
            }

            MessageContract.UiEvents.AttachmentsClicked -> {

            }

            MessageContract.UiEvents.CameraClicked -> {

            }
        }
    }

    private fun initialUiState() = MessageContract.UiState(
        input = "",
        senderUid = null,
        senderRoom = null,
        receiverRoom = null,
        receiverId = null,
        model = null
    )
}
