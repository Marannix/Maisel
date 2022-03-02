package com.maisel.compose.state.user.compose

import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserComposerController @Inject constructor(
    private val lastMessageUseCase: GetLastMessageUseCase,
    private val userRepository: UserRepository
) {

    init {
        //TODO: Get list of users and other information here?
    }

    /**
     * Creates a [CoroutineScope] that allows us to cancel the ongoing work when the parent
     * ViewModel is disposed.
     */
    private val scope = CoroutineScope(DispatcherProvider.Main)

    /**
     * Represents the user Logged In
     */
    val currentUser: MutableStateFlow<User> = MutableStateFlow(User())

    /**
     * Represents the list of users from Firebase Realtime Database
     */
    val users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    /**
     * Represents the list of latest messages from Firebase Realtime Database
     */
    val latestMessages: MutableStateFlow<List<MessageModel>> = MutableStateFlow(emptyList())

    /**
     * Retrieve current user
     */
    fun getLoggedInUser() {
        scope.launch {
            //TODO: Create Usecase
            userRepository.getCurrentUser().collect { result ->
                result.onSuccess {
                    currentUser.value = it
                }

                result.onFailure { throwable ->
                    //TODO: Update UI and show error
                }
            }
        }
    }

    /**
     * Retrieve and set last message for a specific user based on their userId
     * @param userId of a user from Firebase Realtime Database
     */
    fun getLatestMessages() {
        scope.launch {
            lastMessageUseCase.invoke().collect { result ->
                result.onSuccess { listOfLatestMessages ->
                    latestMessages.value = listOfLatestMessages

                    result.onFailure { throwable ->
                        //TODO: Update UI and show error?
                    }
                }
            }
        }
    }

    /**
     * Retrieve list of users from Firebase Realtime Database
     */
    fun listOfUsers() {
        scope.launch {
            //TODO: Create Usecase
            userRepository.fetchListOfUsers().collect { result ->
                result.onSuccess { listOfUsers ->
                    userRepository.insertUsers(listOfUsers)
                }
                result.onFailure { throwable ->
                    //TODO: Update UI and show error
                }
            }
        }
    }

    fun getUsers() {
        scope.launch {
            users.value = userRepository.getUsers()
        }
    }


    /**
     * Cancels any pending work when the parent ViewModel is about to be destroyed.
     */
    fun onCleared() {
        scope.cancel()
    }
}
