package com.maisel.dashboard

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.compose.state.user.compose.UserComposerController
import com.maisel.dashboard.chat.DashboardViewState
import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.usecase.GetUsersUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val usersUseCase: GetUsersUseCase,
    private val userComposerController: UserComposerController
) : BaseViewModel() {

    val viewState = MutableLiveData<DashboardViewState>()

    val users: StateFlow<List<SignUpUser>> = userComposerController.users

    val recentUsers: StateFlow<List<SignUpUser>> = userComposerController.recentUsers

    init {
        viewState.value = DashboardViewState()
        userComposerController.listOfUsers()
        userComposerController.getLastMessagesUseCaseV2()
        userComposerController.findUserLastMessageV2()
    }

    private fun currentViewState(): DashboardViewState = viewState.value!!

    fun logOutUser() {
        logOutUseCase.invoke()
    }

    fun getUsers() {
        usersUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                viewState.value =
                    currentViewState().copy(use = GetUsersUseCase.UserDataState.Loading)
            }
            .subscribe {
                viewState.value = currentViewState().copy(use = it)
            }
            .addDisposable()
    }

    fun startListeningToUser() {
        usersUseCase.startListeningToUsers()
    }

    fun stopListeningToUser() {
        usersUseCase.stopListeningToUsers()
    }

    /**
     * Disposes the inner [DashboardViewModel].
     */
    override fun onCleared() {
        super.onCleared()
        userComposerController.onCleared()
    }
}
