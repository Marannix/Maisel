package com.maisel.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.maisel.common.BaseViewModel
import com.maisel.state.AuthResultState
import com.maisel.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : BaseViewModel() {
    val viewState = MutableLiveData<AuthResultState>()

    fun registerUser(email: String, address: String, password: String) {
   //     if (viewState.value == null || viewState.value == AuthResultState.Idle) {
            signUpUseCase.invoke(email, address, password)
                .observeOn(AndroidSchedulers.mainThread())
//                .map { result ->
//                    when (result) {
//                        AuthResultState.Error -> {
//                            Log.d("joshua", "authresultstate error")
//                            AuthResultState.Error
//                        }
//                        AuthResultState.Loading -> {
//                            Log.d("joshua", "authresultstate loading")
//                            AuthResultState.Loading
//                        }
//                        AuthResultState.Success -> {
//                            Log.d("joshua", "authresultstate success")
//                            AuthResultState.Success
//                        }
//                        AuthResultState.Idle -> {
//                            Log.d("joshua", "authresultstate idle")
//                            AuthResultState.Idle
//                        }
//                    }
                .doOnSubscribe { viewState.value = AuthResultState.Loading }
                .subscribe ({
                    Log.d("joshua", it.toString())
                    viewState.value = AuthResultState.Success

                }, {
              Log.d("joshua", "authresultstate " + it.message)
                    viewState.value = AuthResultState.Error

                })
               //     viewState.value = it
                .addDisposable()
//            railwayChildrenWordsUseCase.invoke()
//                .observeOn(AndroidSchedulers.mainThread())
//                .map { state ->
//                    when (state) {
//                        is GetRailwayChildrenWordsUseCase.RailwayChildrenDataState.Success -> {
//                            DashboardState.RailwayChildrenBook(state.listOfWords)
//                        }
//                        is GetRailwayChildrenWordsUseCase.RailwayChildrenDataState.Error -> {
//                            DashboardState.Error(state.message, state.bookFromLocalStorage)
//                        }
//                    }
//                }
//                .doOnSubscribe { viewState.value = DashboardState.Loading }
//                .subscribe {
//                    viewState.value = it
//                }.addDisposable()
        }
    }
}