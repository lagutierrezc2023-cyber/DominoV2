package com.example.domino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.domino.navigation.AppNavigation
import com.example.domino.ui.theme.DominoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DominoTheme {
                // Surface pone el color de fondo del tema en todas las pantallas
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // systemBarsPadding deja espacio para la barra de arriba y la de abajo
                    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}