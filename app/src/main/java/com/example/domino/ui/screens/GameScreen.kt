package com.example.domino.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domino.ui.components.BoardView
import com.example.domino.ui.components.GameControls
import com.example.domino.ui.components.PlayerHand
import com.example.domino.ui.components.ScoreBoard
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

    LaunchedEffect(Unit) { viewModel.start(roomCode) }
    LaunchedEffect(state.status) {
        if (state.status == "FINISHED") onGameOver()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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