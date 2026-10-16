package com.example.domino.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domino.viewModel.LobbyViewModel

@Composable
fun LobbyScreen(viewModel: LobbyViewModel, onStartGame: () -> Unit) {

    var isJoining by remember { mutableStateOf(false) }

    var title = "Crear sala"
    if (isJoining) {
        title = "Unirse a una sala"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.playerName,
            onValueChange = { viewModel.playerName = it },
            label = { Text("Tu nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isJoining) {
            OutlinedTextField(
                value = viewModel.roomCode,
                onValueChange = { viewModel.roomCode = it },
                label = { Text("Codigo de la sala") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!isJoining && viewModel.roomCode != "") {
            Text(text = "Codigo de tu sala: " + viewModel.roomCode)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (viewModel.errorMessage != "") {
            Text(text = viewModel.errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (isJoining) {
                    viewModel.joinRoom(onStartGame)
                } else {
                    viewModel.createRoom { onStartGame() }
                }
            },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isJoining) {
                Text("Unirse")
            } else {
                Text("Crear sala")
            }
        }

        TextButton(onClick = {
            isJoining = !isJoining
            viewModel.clearFields()
        }) {
            if (isJoining) {
                Text("¿No tienes codigo? Crea una sala")
            } else {
                Text("¿Tienes un codigo? Unete a una sala")
            }
        }
    }
}