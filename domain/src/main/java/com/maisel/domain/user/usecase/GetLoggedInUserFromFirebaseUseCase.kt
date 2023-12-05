package com.maisel.domain.user.usecase

import android.util.Log
import com.maisel.domain.user.entity.User
import com.maisel.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class GetLoggedInUserFromFirebaseUseCase @Inject constructor(
    private val userRepository: UserRepository,
  //  private val storeAuthUserInLocalDbUseCase: StoreAuthUserInLocalDbUseCase,
) {
    suspend operator fun invoke(): Flow<Result<User>> {
        return userRepository.listenToLoggedInUser()
            //.collectLatest { result ->
          //  Log.d("joshua logged in: ", result.getOrThrow().toString())
            //return user
       //     storeAuthUserInLocalDbUseCase.invoke(result.getOrThrow())
        //    return result.getOrThrow()
     //   }
    }

    fun getLoggedInUser(): User? {
        return userRepository.getLoggedInUser()
    }
}
