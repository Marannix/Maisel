package com.maisel.domain.database

sealed class ApplicationCacheState {
    object Loading : ApplicationCacheState()

    class Loaded(val settings: ApplicationSetting) : ApplicationCacheState()

    object Error : ApplicationCacheState()
}
