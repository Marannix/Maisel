package com.maisel.chat

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.domain.user.entity.SignUpUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor() : BaseViewModel() {

    val viewState = MutableLiveData<ChatDetailViewState>()

    init {
        viewState.value = ChatDetailViewState()
    }

    private fun currentViewState(): ChatDetailViewState = viewState.value!!

    fun setUser(user: SignUpUser) {
        viewState.value = currentViewState().copy(user = user)
    }
}
