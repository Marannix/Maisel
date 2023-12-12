package com.maisel.dashboard

sealed class DashboardDestination {
    data class ChatDetail(val receiverUserId: String) : DashboardDestination()
    object Settings : DashboardDestination()
}
