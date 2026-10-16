package com.example.domino.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dominoconquest.ui.screens.GameScreen
import com.example.dominoconquest.ui.screens.LobbyScreen
import com.example.dominoconquest.ui.screens.LoginScreen
import com.example.dominoconquest.ui.screens.ResultScreen
import com.example.dominoconquest.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel = remember { LoginViewModel() }

    var startScreen = "login"
    if (FirebaseAuth.getInstance().currentUser != null) {
        startScreen = "lobby"
    }

    NavHost(navController = navController, startDestination = startScreen) {

        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("lobby") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("lobby") {
            LobbyScreen(
                onStartGame = { navController.navigate("game") }
            )
        }

        composable("game") {
            GameScreen(
                onGameOver = { navController.navigate("result") },
                onLeave = { navController.navigate("lobby") }
            )
        }

        composable("result") {
            ResultScreen(
                onBackToMenu = { navController.navigate("lobby") }
            )
        }
    }
}