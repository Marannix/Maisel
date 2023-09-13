package com.maisel.main

import com.maisel.common.base.BaseViewModel
import com.maisel.domain.user.usecase.GetLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    appPreferences: AppPreferences,
    private val loggedInUser: GetLoggedInUserUseCase
) : BaseViewModel() {

    val hasSeenShowcase: Flow<Boolean> = appPreferences.hasSeenShowcase

    fun isUserLoggedIn(): Boolean {
        return loggedInUser.getLoggedInUser() != null
    }
}
