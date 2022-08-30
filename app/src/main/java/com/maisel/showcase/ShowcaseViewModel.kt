package com.maisel.showcase

import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(private val loggedInUser: GetLoggedInUserUseCase): BaseViewModel() {

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }
}
