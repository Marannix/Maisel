package com.maisel.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class DashboardDrawerMenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    companion object {
        fun get(): List<DashboardDrawerMenuItem> {
            return listOf(
                DashboardDrawerMenuItem(
                    id = "contacts",
                    title = "Contacts",
                    contentDescription = "Go to contacts screen",
                    icon = Icons.Default.Person
                ),
                DashboardDrawerMenuItem(
                    id = "calls",
                    title = "Calls",
                    contentDescription = "Go to calls screen",
                    icon = Icons.Default.Call
                ),
                DashboardDrawerMenuItem(
                    id = "settings",
                    title = "Settings",
                    contentDescription = "Go to settings screen",
                    icon = Icons.Default.Settings
                ),
                DashboardDrawerMenuItem(
                    id = "help",
                    title = "Help",
                    contentDescription = "Get help",
                    icon = Icons.Default.Info
                )
            )
        }
    }
}
