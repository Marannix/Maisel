package com.maisel.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.maisel.navigation.Screens

data class DashboardDrawerMenuItem(
    val screen: Screens,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    companion object {
        fun get(): List<DashboardDrawerMenuItem> {
            return listOf(
                DashboardDrawerMenuItem(
                    screen = Screens.Contact,
                    title = "Contacts",
                    contentDescription = "Go to contacts screen",
                    icon = Icons.Default.Person
                ),
                DashboardDrawerMenuItem(
                    screen = Screens.Placeholder,
                    title = "Calls",
                    contentDescription = "Go to calls screen",
                    icon = Icons.Default.Call
                ),
                DashboardDrawerMenuItem(
                    screen = Screens.Placeholder,
                    title = "Settings",
                    contentDescription = "Go to settings screen",
                    icon = Icons.Default.Settings
                ),
                DashboardDrawerMenuItem(
                    screen = Screens.Placeholder,
                    title = "Help",
                    contentDescription = "Get help",
                    icon = Icons.Default.Info
                )
            )
        }
    }
}
