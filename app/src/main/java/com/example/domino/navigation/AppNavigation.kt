package com.example.domino.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.domino.data.repository.GameRepositoryImpl
import com.example.domino.domain.GameEndChecker
import com.example.domino.domain.GameEngine
import com.example.domino.ui.screens.GameScreen
import com.example.domino.ui.screens.LobbyScreen
import com.example.domino.ui.screens.LoginScreen
import com.example.domino.ui.screens.ResultScreen
import com.example.domino.viewModel.GameViewModel
import com.example.domino.viewModel.LobbyViewModel
import com.example.domino.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel = remember { LoginViewModel() }
    val lobbyViewModel = remember { LobbyViewModel(GameRepositoryImpl()) }

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
                viewModel = lobbyViewModel,
                onStartGame = {
                    val code = lobbyViewModel.roomCode.trim().uppercase()
                    navController.navigate("game/" + code)
                }
            )
        }

        composable("game/{code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            GameScreen(
                roomCode = code,
                onGameOver = { navController.navigate("result/" + code) },
                onLeave = {
                    navController.navigate("lobby") {
                        popUpTo("lobby") { inclusive = true }
                    }
                }
            )
        }

        composable("result/{code}") { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""
            val gameViewModel: GameViewModel = viewModel()
            val state by gameViewModel.state.collectAsState()
            val checker = GameEndChecker(GameEngine())

            LaunchedEffect(Unit) { gameViewModel.start(code) }

            if (state.players.isNotEmpty()) {
                ResultScreen(
                    players = state.players,
                    winner = checker.getWinner(state),
                    reason = checker.getReason(state),
                    onRestart = {
                        gameViewModel.restart()
                        navController.navigate("game/" + code)
                    },
                    onBackToMenu = {
                        navController.navigate("lobby") {
                            popUpTo("lobby") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}