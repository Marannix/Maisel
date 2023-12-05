package com.maisel.dashboard

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.user.repository.UserRepository
import com.maisel.domain.user.usecase.FetchListOfUsersUseCase
import com.maisel.domain.user.usecase.GetLoggedInUserFromFirebaseUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import com.maisel.domain.user.usecase.StoreAuthUserInLocalDbUseCase
import com.maisel.state.UserAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val lastMessageUseCase: GetLastMessageUseCase,
    private val fetchListOfUsersUseCase: FetchListOfUsersUseCase,
    private val messageRepository: MessageRepository,
    private val logOutUseCase: LogOutUseCase,
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val getLoggedInUserFromFirebaseUseCase: GetLoggedInUserFromFirebaseUseCase,
    private val storeAuthUserInLocalDbUseCase: StoreAuthUserInLocalDbUseCase,
//    private val clearRoomDatabaseUseCase: ClearRoomDatabaseUseCase,
//    private val getUsersUseCase: GetUsersUseCase
) : DashboardContract.ViewModel() {

    private val _destination = MutableSharedFlow<DashboardDestination>()
    val destination = _destination.asSharedFlow()

    override val _uiState: MutableStateFlow<DashboardContract.UiState> = MutableStateFlow(
        initialUiState()
    )

    init {
        getLoggedInUser()
    }

    private fun loadCache() {
        viewModelScope.launch {
            getApplicationCacheStateUseCase.invoke().collectLatest { cache ->
                when (cache) {
                    ApplicationCacheState.Error -> {
                        Log.d("joshua cache: ", "error")
                        // Log user out
                    }

                    is ApplicationCacheState.Loaded -> {
                        Log.d("joshua cache: ", cache.settings.toString())

                        updateUiState { oldState -> oldState.copy(currentUser = cache.settings.user) }
                        getUsersFromFirebase()
                        getUsersFromLocalStorage()
                        listenToRecentMessages()
                        getRecentMessages()
                    }

                    ApplicationCacheState.Loading -> {
                        Log.d("joshua cache: ", "loading")
                        // Create loading screen
                    }
                }
            }
        }
    }

    /**
     * Set logged in user
     */
    private fun getLoggedInUser() {
        viewModelScope.launch {
            try {
                getLoggedInUserFromFirebaseUseCase.invoke()
                    .collectLatest { result ->
                        val user = result.getOrThrow()
                        storeAuthUserInLocalDbUseCase.invoke(user)
                        loadCache()
                    }
            } catch (throwable: Throwable) {
                // Failed to log in user
                // TODO: Log user out
                Log.d("Joshua logged in: ", throwable.toString())
            }
        }
    }

    /**
     * Retrieve list of users from Firebase Realtime Database
     */
    private fun getUsersFromFirebase() {
        viewModelScope.launch {
            try {
                fetchListOfUsersUseCase.invoke()
            } catch (throwable: Throwable) {
                Log.d("Users Firebase: ", throwable.toString())
            }
        }
    }

    private fun getUsersFromLocalStorage() {
        viewModelScope.launch {
            userRepository.getUsers().collectLatest { contacts ->
                updateUiState { oldState -> oldState.copy(contacts = contacts) }
            }
        }
    }

    /**
     * Retrieve and set last message for a specific user based on their userId
     */
    private fun listenToRecentMessages() {
        viewModelScope.launch {
            try {
                lastMessageUseCase.invoke()
            } catch (throwable: Throwable) {
                Log.d("Recent Message: ", throwable.toString())
            }
        }
    }

    private fun getRecentMessages() {
        viewModelScope.launch {
            messageRepository.getRecentMessages()
                .collectLatest { listOfMessages ->
                    updateUiState { oldState ->
                        oldState.copy(
                            recentMessageState = RecentMessageState.Success(
                                listOfMessages
                            )
                        )
                    }
                }
        }
    }

    private fun logOutUser() {
        viewModelScope.launch {
            logOutUseCase.invoke().collectLatest {

            }
        }
    }

    fun getMenuItems(): List<DashboardDrawerMenuItem> {
        return DashboardDrawerMenuItem.get()
    }

    override fun onUiEvent(event: DashboardContract.UiEvents) {
        when (event) {
            is DashboardContract.UiEvents.EmailUpdated -> {
                // NO-OP
            }

            is DashboardContract.UiEvents.RecentMessageClicked -> {
                viewModelScope.launch {
                    if (event.receiverUserId != null) {
                        _destination.emit(DashboardDestination.ChatDetail(event.receiverUserId))
                    } else {
                        //TODO: Error screen
                    }
                }
            }
        }
    }

    private fun initialUiState() = DashboardContract.UiState(
        isLoading = false,
        currentUser = null,
        contacts = emptyList(),
        recentMessageState = RecentMessageState.Loading,
        userAuthState = UserAuthState.EMPTY
    )
}
