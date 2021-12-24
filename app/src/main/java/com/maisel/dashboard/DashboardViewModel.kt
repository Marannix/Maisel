package com.maisel.dashboard

import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.dashboard.chat.DashboardViewState
import com.maisel.domain.user.usecase.GetUsersUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val logOutUseCase: LogOutUseCase, private val usersUseCase: GetUsersUseCase ): BaseViewModel(){

    val viewState = MutableLiveData<DashboardViewState>()

    init {
        viewState.value = DashboardViewState()
    }

    private fun currentViewState(): DashboardViewState = viewState.value!!

    fun logOutUser() {
        logOutUseCase.invoke()
    }

    fun getUsers() {
        usersUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { viewState.value = currentViewState().copy(use = GetUsersUseCase.UserDataState.Loading) }
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
}
