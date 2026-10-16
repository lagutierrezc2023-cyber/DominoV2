package com.example.domino.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domino.data.model.Player

@Composable
fun ResultScreen(
    players: List<Player>,
    winner: Player?,
    reason: String,
    onRestart: () -> Unit,
    onBackToMenu: () -> Unit
) {
    var title = "¡Empate!"
    if (winner != null) {
        title = "¡Ganó " + winner.name + "!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Fin de la partida")
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (reason != "") {
            Text(text = reason)
            Spacer(modifier = Modifier.height(16.dp))
        }

        for (player in players) {
            Text(
                text = player.name + ": " + player.score + " puntos, " +
                        player.territories + " territorios"
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onRestart() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Jugar otra vez")
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onBackToMenu() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menú")
        }
    }
}