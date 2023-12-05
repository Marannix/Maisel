package com.maisel.main

import androidx.lifecycle.viewModelScope
import com.maisel.common.base.BaseViewModel
import com.maisel.domain.database.usecase.LoadApplicationCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val loadApplicationCacheUseCase: LoadApplicationCacheUseCase,
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            loadApplicationCacheUseCase.invoke()
        }
    }
}
