package com.maisel.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatcherProvider {

    var Main: CoroutineDispatcher = Dispatchers.Main
        internal set

    var IO: CoroutineDispatcher = Dispatchers.IO
        internal set
}
