package com.example.domino.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domino.ui.components.BoardView
import com.example.domino.ui.components.GameControls
import com.example.domino.ui.components.PlayerHand
import com.example.domino.ui.components.ScoreBoard
import com.example.domino.ui.components.playerColor
import com.example.domino.viewModel.GameViewModel

@Composable
fun GameScreen(
    roomCode: String,
    onGameOver: () -> Unit,
    onLeave: () -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val piece by viewModel.selectedPiece.collectAsState()
    val cells by viewModel.selectedCells.collectAsState()
    val message by viewModel.message.collectAsState()
    val me = state.players.find { it.id == viewModel.myId }

    // true cuando ya vimos la partida en juego
    var wasPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.start(roomCode) }
    LaunchedEffect(state.status) {
        if (state.status == "PLAYING") {
            wasPlaying = true
        }
        if (state.status == "FINISHED" && wasPlaying) {
            onGameOver()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (state.status == "WAITING") {
            Text("Sala de partida", style = MaterialTheme.typography.titleLarge)

            // Tarjeta con el código de la sala
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Código de la sala")
                    Text(
                        text = roomCode,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 6.sp
                    )
                    Text("Compártelo para que otro jugador se una")
                }
            }

            // Tarjeta con los jugadores
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Jugadores (" + state.players.size + "/4)",
                        fontWeight = FontWeight.Bold
                    )
                    for (i in state.players.indices) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(playerColor(i), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(state.players[i].name)
                        }
                    }
                }
            }

            // Solo el anfitrión (el primer jugador) puede empezar
            val isHost = state.players.isNotEmpty() && state.players[0].id == viewModel.myId
            if (isHost) {
                Button(
                    onClick = viewModel::startGame,
                    enabled = state.players.size >= 2,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Empezar partida")
                }
                if (state.players.size < 2) {
                    Text("Esperando a otro jugador...")
                }
            } else {
                Text("Esperando a que el anfitrión empiece la partida...")
            }
        } else {
            ScoreBoard(state.players, state.currentTurn, state.round, state.maxRounds)
            BoardView(state.board, state.players, cells, viewModel::selectCell)
            PlayerHand(me?.hand ?: emptyList(), piece, viewModel::selectPiece)

            if (message != "") {
                Text(message)
            }

            GameControls(
                canPlace = piece != null && cells.size == 2 && viewModel.isMyTurn(),
                onPlace = viewModel::place,
                onRotate = viewModel::rotate,
                onCancel = viewModel::cancel
            )

            Button(
                onClick = viewModel::drawPiece,
                enabled = viewModel.canDraw(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.pool.isEmpty()) "Pasar turno" else "Tomar ficha del pozo")
            }
        }

        OutlinedButton(
            onClick = {
                viewModel.leave()
                onLeave()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abandonar partida")
        }
    }
}