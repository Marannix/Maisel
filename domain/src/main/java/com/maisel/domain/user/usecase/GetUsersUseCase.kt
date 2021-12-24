package com.maisel.domain.user.usecase

import com.maisel.domain.user.entity.SignUpUser
import com.maisel.domain.user.repository.UserRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository){
    operator fun invoke(): Observable<UserDataState> {
        return userRepository.observeListOfUsers().map<UserDataState> {
            UserDataState.Success(it)
        }.onErrorReturn {
            UserDataState.Error
        }
    }

    fun startListeningToUsers() {
        userRepository.startListeningToUsers()
    }

    fun stopListeningToUsers() {
        userRepository.stopListeningToUsers()
    }

    sealed class UserDataState {
        object Loading: UserDataState()
        data class Success(val user: List<SignUpUser>): UserDataState()
        object Error : UserDataState()
    }
}
