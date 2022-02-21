package com.maisel.compose.state.user.compose

import android.util.Log
import com.maisel.coroutine.DispatcherProvider
import com.maisel.domain.message.MessageModel
import com.maisel.domain.message.usecase.GetLastMessageUseCase
import com.maisel.domain.user.entity.SignUpUser
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
     * Represents the list of users from Firebase Realtime Database
     */
    val users: MutableStateFlow<List<SignUpUser>> = MutableStateFlow(emptyList())

    val recentUsers: MutableStateFlow<List<SignUpUser>> = MutableStateFlow(emptyList())

    /**
     * Represents the list of latest messages from Firebase Realtime Database
     */
    val latestMessages: MutableStateFlow<List<MessageModel>> = MutableStateFlow(emptyList())

    /**
     * Represents the list of latest messages from Firebase Realtime Database
     */
    val latestMessagesv3: MutableStateFlow<List<MessageModel>> = MutableStateFlow(emptyList())


    /**
     * Retrieve and set last message for a specific user based on their userId
     * @param userId of a user from Firebase Realtime Database
     */
    fun getLastMessagesUseCaseV2() {
        scope.launch {
            lastMessageUseCase.invoke().collect { result ->
                result.onSuccess { listOfLatestMessages ->
                    latestMessages.value = listOfLatestMessages

                    users.collect { users ->
                        users.forEach { user ->
                            user.userId?.let { userId ->
                                user.lastMessage =
                                    listOfLatestMessages.firstOrNull { it.uid == userId }?.message
                                Log.d("JoshuaTest", user.lastMessage.toString())
                            }
                        }
                    }
                    result.onFailure { throwable ->
                        //TODO: Update UI and show error?
                    }
                }
            }
        }
    }

    fun getLastMessagesUseCaseV3() {
        scope.launch {
            lastMessageUseCase.invoke().collect { result ->
                result.onSuccess { listOfLatestMessages ->
                    latestMessages.value = listOfLatestMessages

                    users.collect { users ->
                        users.forEach { user ->
                            user.userId?.let { userId ->
                                user.lastMessage =
                                    listOfLatestMessages.firstOrNull { it.uid == userId }?.message
                                Log.d("JoshuaTest", user.lastMessage.toString())
                            }
                        }
                    }
                    result.onFailure { throwable ->
                        //TODO: Update UI and show error?
                    }
                }
            }
        }
    }
//    private fun getLastMessagesUseCase(userId: String) {
//
//        scope.launch {
//            lastMessageUseCase.invoke().collect { result ->
//                result.onSuccess { lastMessage ->
//
//
//                    users.collect { listOfUsers ->
//                        listOfUsers.toMutableList().map {
//                            if (it.userId == userId) {
//                               it = it.copy(lastMessage = lastMessage)
//                            }
//                        }
//                        listOfUsers.find { it.userId == userId }?.lastMessage = lastMessage
//
//                        users.value = listOfUsers
//                    }
//                }
//            }
//
//            merge(users, latestMessages).collect {
//
//            }
//        }
//    }

        /**
         * Retrieve list of users from Firebase Realtime Database
         */
        fun listOfUsers() {
            scope.launch {
                //TODO: Create Usecase
                userRepository.fetchListOfUsers().collect { result ->
                    result.onSuccess { listOfUsers ->
                        users.value = listOfUsers
                    }
                    result.onFailure { throwable ->
                        //TODO: Update UI and show error
                    }
                }
            }
        }

        fun findUserLastMessage(userId: String) {
            scope.launch {
                latestMessages.collect {
                    it.first { it.uid == userId }.message
                }
            }
        }

    fun findUserLastMessageV2() {
        scope.launch {
            val list = mutableListOf<SignUpUser>()
            latestMessages.collect { messages ->
                users.collect { users ->
                    users.forEach { user ->
                        user.userId?.let { userId ->
                            user.lastMessage = messages.firstOrNull { it.uid == userId }?.message
                            list.add(user)
                            Log.d("JoshuaTest", user.lastMessage.toString())
                        }
                    }
                }
            }

            recentUsers.value = list
        }
    }

        /**
         * Retrieve list of users from Firebase Realtime Database
         */
//        fun stuff() {
//            scope.launch {
//                users.collect {
//                    it.forEach { user ->
//                     user.lastMessage = messages.firstOrNull { it.uid == userId }?.message
//                    }
//                }
//            }
//        }

        /**
         * Cancels any pending work when the parent ViewModel is about to be destroyed.
         */
        fun onCleared() {
            scope.cancel()
        }
}
