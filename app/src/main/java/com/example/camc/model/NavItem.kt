package com.example.camc.model

import androidx.compose.ui.graphics.painter.Painter


data class NavItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val badgeCount: Int? = null,
    val route: String
)