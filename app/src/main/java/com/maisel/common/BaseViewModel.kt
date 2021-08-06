package com.maisel.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    protected fun Disposable.addDisposable() = compositeDisposable.add(this)

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}