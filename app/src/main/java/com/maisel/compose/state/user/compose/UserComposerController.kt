package com.maisel.compose.state.user.compose

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.maisel.coroutine.DispatcherProvider
import com.maisel.dashboard.DashboardViewState
import com.maisel.dashboard.RecentMessageState
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import com.maisel.domain.user.usecase.GetLoggedInUser
import com.maisel.domain.user.usecase.LogOutUseCase
import com.maisel.state.UserAuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserComposerController @Inject constructor(
    private val lastMessageUseCase: GetLastMessageUseCase,
    private val getLoggedInUser: GetLoggedInUser,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val logOutUseCase: LogOutUseCase
) {

    /**
     * Creates a [CoroutineScope] that allows us to cancel the ongoing work when the parent
     * ViewModel is disposed.
     */
    private val scope = CoroutineScope(DispatcherProvider.Main)

    /**
     * UI state of the current composer view state.
     */
    private val _stateFlow: MutableStateFlow<DashboardViewState> =
        MutableStateFlow(DashboardViewState())

    val state: StateFlow<DashboardViewState>
        get() = _stateFlow

    /**
     * Represents the user Logged In
     */
    val currentUser: MutableStateFlow<User> = MutableStateFlow(User())

    /**
     * Represents the list of users from Firebase Realtime Database
     */
    val users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    /**
     * Represents logged out state
     */
    val loggedState: MutableStateFlow<UserAuthState> = MutableStateFlow(UserAuthState.EMPTY)

    /**
     * Represents the list of latest messages from Firebase Realtime Database
     */
    private var latestMessages: MutableStateFlow<List<MessageModel>> = MutableStateFlow(emptyList())

    /**
     * Set logged in user
     */
    fun setLoggedInUser() {
        scope.launch { getLoggedInUser.invoke() }
    }

    /**
     * Retrieve logged in user
     */
    fun getLoggedInUser() {
        scope.launch {
            getLoggedInUser.getLoggedInUser()?.let { user ->
                currentUser.value = user
            }
        }
    }

    /**
     * Retrieve and set last message for a specific user based on their userId
     */
    fun listenToRecentMessages() {
        scope.launch {
            lastMessageUseCase.invoke().collect { result ->
                result.onSuccess { listOfLatestMessages ->
                    messageRepository.insertRecentMessages(listOfLatestMessages.toMutableStateList())
                }
                result.onFailure { throwable ->
                    //TODO: Update UI and show error?
                }
            }
        }
    }

    fun getRecentMessages() {
        scope.launch {
            _stateFlow.update { it.copy(recentMessageState = RecentMessageState.Loading) }
            messageRepository.getRecentMessages()
                .collect { listOfMessages ->
                    _stateFlow.value = _stateFlow.value.copy(
                        recentMessageState = RecentMessageState.Success(listOfMessages)
                    )
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
                    Log.d("Maisel: ", throwable.toString())

                }
            }
        }
    }

    fun getUsers() {
        scope.launch {
            userRepository.getUsers().collect { listOfUsers ->
                users.emit(listOfUsers)
            }
        }
    }

    fun logoutUser() {
        scope.launch {
            userRepository.logoutUser().collect { result ->
                result.onSuccess {
                    _stateFlow.update { it.copy(userAuthState = UserAuthState.LOGGED_OUT) }
                }
                result.onFailure { throwable ->
                    _stateFlow.update { it.copy(userAuthState = UserAuthState.LOGGED_OUT) }
                }
            }
        }
    }

    /**
     * Cancels any pending work when the parent ViewModel is about to be destroyed.
     */
    fun onCleared() {
        scope.cancel()
    }
}
