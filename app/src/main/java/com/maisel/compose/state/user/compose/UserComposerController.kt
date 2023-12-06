package com.maisel.compose.state.user.compose

import com.maisel.coroutine.DispatcherProvider
import com.maisel.dashboard.DashboardViewState
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.room.ClearRoomDatabaseUseCase
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import com.maisel.domain.user.usecase.GetLoggedInUserFromFirebaseUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserComposerController @Inject constructor(
    private val lastMessageUseCase: GetLastMessageUseCase,
    private val getLoggedInUser: GetLoggedInUserFromFirebaseUseCase,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val logOutUseCase: LogOutUseCase,
    private val clearRoomDatabaseUseCase: ClearRoomDatabaseUseCase
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
 //   val currentUser: MutableStateFlow<User> = MutableStateFlow(User())

    /**
     * Represents the list of users from Firebase Realtime Database
     */
    val users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    /**
     * Set logged in user
     */
//    fun setLoggedInUser() {
//        scope.launch {
//            getLoggedInUser.invoke().collect { result ->
//                result.onSuccess {
//                    currentUser.value = it
//                }
//                result.onFailure {
//                    getStoredLoggedInUser()
//                }
//            }
//        }
//    }

    /**
     * Retrieve offline logged in user
     */
//    private fun getStoredLoggedInUser() {
//        scope.launch {
//            getLoggedInUser.getLoggedInUser()?.let { user ->
//                currentUser.value = user
//            }
//        }
//    }

    /**
     * Retrieve and set last message for a specific user based on their userId
     */
//    fun listenToRecentMessages() {
//        scope.launch {
//            lastMessageUseCase.invoke().collect { result ->
//                result.onSuccess { listOfLatestMessages ->
//                    messageRepository.insertRecentMessages(listOfLatestMessages.toMutableStateList())
//                }
//                result.onFailure { throwable ->
//                    //TODO: Update UI and show error?
//                }
//            }
//        }
//    }

//    fun getRecentMessages() {
//        scope.launch {
//            _stateFlow.update { it.copy(recentMessageState = RecentMessageState.Loading) }
//            messageRepository.getRecentMessages()
//                .collect { listOfMessages ->
//                    _stateFlow.value = _stateFlow.value.copy(
//                        recentMessageState = RecentMessageState.Success(listOfMessages)
//                    )
//                }
//        }
//    }

    /**
     * Retrieve list of users from Firebase Realtime Database
     */
//    fun listOfUsers() {
//        scope.launch {
//            //TODO: Create Usecase
//            userRepository.fetchListOfUsers().collect { result ->
//                result.onSuccess { listOfUsers ->
//                    userRepository.insertUsers(listOfUsers)
//                }
//                result.onFailure { throwable ->
//                    //TODO: Update UI and show error
//                    Log.d("Maisel: ", throwable.toString())
//
//                }
//            }
//        }
//    }

//    fun getUsers() {
//        scope.launch {
//            userRepository.getUsers().collect { listOfUsers ->
//                users.emit(listOfUsers)
//            }
//        }
//    }

//    fun logoutUser() {
//        scope.launch {
//            userRepository.logoutUser().collect { result ->
//                clearRoomDatabaseUseCase.invoke()
//                result.onSuccess {
//                    _stateFlow.update { it.copy(userAuthState = UserAuthState.LOGGED_OUT) }
//                }
//                result.onFailure { throwable ->
//                    _stateFlow.update { it.copy(userAuthState = UserAuthState.LOGGED_OUT) }
//                }
//            }
//        }
//    }

    /**
     * Cancels any pending work when the parent ViewModel is about to be destroyed.
     */
//    fun onCleared() {
//        scope.cancel()
//    }
}
