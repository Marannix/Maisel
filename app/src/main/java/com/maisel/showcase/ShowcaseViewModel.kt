package com.maisel.showcase

import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.GetLoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShowcaseViewModel @Inject constructor(private val loggedInUser: GetLoggedInUser): BaseViewModel() {

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }
}
