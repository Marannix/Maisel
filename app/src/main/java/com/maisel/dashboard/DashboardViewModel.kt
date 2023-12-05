package com.maisel.dashboard

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.maisel.domain.database.ApplicationCacheState
import com.maisel.domain.database.usecase.GetApplicationCacheStateUseCase
import com.maisel.domain.message.MessageRepository
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.room.ClearRoomDatabaseUseCase
import com.maisel.domain.user.repository.UserRepository
import com.maisel.domain.user.usecase.ClearLocalUserUseCase
import com.maisel.domain.user.usecase.FetchListOfUsersUseCase
import com.maisel.domain.user.usecase.GetLoggedInUserFromFirebaseUseCase
import com.maisel.domain.user.usecase.GetUsersUseCase
import com.maisel.domain.user.usecase.LogOutUseCase
import com.maisel.domain.user.usecase.StoreAuthUserInLocalDbUseCase
import com.maisel.state.UserAuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val lastMessageUseCase: GetLastMessageUseCase,
    private val fetchListOfUsersUseCase: FetchListOfUsersUseCase,
    private val messageRepository: MessageRepository,
    private val logOutUseCase: LogOutUseCase,
    private val getApplicationCacheStateUseCase: GetApplicationCacheStateUseCase,
    private val getLoggedInUserFromFirebaseUseCase: GetLoggedInUserFromFirebaseUseCase,
    private val storeAuthUserInLocalDbUseCase: StoreAuthUserInLocalDbUseCase,
    private val getUsersUseCase: GetUsersUseCase,
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
                        logOutUser()
                    }

                    is ApplicationCacheState.Loaded -> {
                        updateUiState { oldState -> oldState.copy(currentUser = cache.settings.user) }
                        getUsersFromFirebase()
                        getUsersFromLocalStorage()
                        listenToRecentMessages()
                        getRecentMessages()
                    }

                    ApplicationCacheState.Loading -> {
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
                logOutUser()
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
            getUsersUseCase.invoke().collectLatest { contacts ->
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
                //TODO: Create an error screen
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
                            recentMessageState = RecentMessageState.Success(listOfMessages)
                        )
                    }
                }
        }
    }

    private fun logOutUser() {
        viewModelScope.launch {
            logOutUseCase.invoke().collectLatest {
                updateUiState { oldState -> oldState.copy(userAuthState = UserAuthState.LOGGED_OUT) }
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

            DashboardContract.UiEvents.LogoutClicked -> {
                logOutUser()
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
