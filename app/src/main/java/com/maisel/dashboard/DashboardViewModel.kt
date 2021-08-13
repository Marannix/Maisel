package com.maisel.dashboard

import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val logOutUseCase: LogOutUseCase): BaseViewModel(){

    fun logOutUser() {
        logOutUseCase.invoke()
    }
}