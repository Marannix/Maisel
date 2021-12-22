package com.maisel.onboarding

import com.maisel.common.BaseViewModel
import com.maisel.domain.user.usecase.GetCurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingCarouselViewModel @Inject constructor(private val currentUser: GetCurrentUser): BaseViewModel() {

    fun isUserLoggedIn(): Boolean {
        return currentUser.invoke() != null
    }
}