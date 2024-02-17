package com.wikicoding.composetodolist.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wikicoding.composetodolist.uiscreens.HomeScreen
import com.wikicoding.composetodolist.viewmodel.TodoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.wikicoding.composespeechtotext.VoiceToTextParser
import com.wikicoding.composetodolist.uiscreens.AddEditTodoScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    voiceToTextParser: VoiceToTextParser,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.AddEditScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) {
            val id = if (it.arguments != null) it.arguments!!.getInt("id") else 0
            AddEditTodoScreen(navController = navController, id = id, voiceToTextParser = voiceToTextParser)
        }
    }
}