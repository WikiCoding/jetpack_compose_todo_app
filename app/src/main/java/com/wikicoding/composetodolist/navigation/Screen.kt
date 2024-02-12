package com.wikicoding.composetodolist.navigation

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object AddEditScreen : Screen("add_edit_screen")
}
