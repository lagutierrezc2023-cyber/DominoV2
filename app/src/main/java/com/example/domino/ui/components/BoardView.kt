package com.example.domino.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domino.data.model.Player
import com.example.domino.data.model.Territory
import com.example.domino.ui.theme.PlayerBlue
import com.example.domino.ui.theme.PlayerGreen
import com.example.domino.ui.theme.PlayerRed
import com.example.domino.ui.theme.PlayerYellow

private const val BOARD_SIZE = 5

@Composable
fun BoardView(
    board: List<Territory>,
    players: List<Player>,
    selected: List<Int> = emptyList(),
    onTerritoryClick: (Territory) -> Unit
) {
    if (board.size < BOARD_SIZE * BOARD_SIZE) {
        Text(text = "Esperando el tablero...")
        return
    }

    // Usamos todo el ancho disponible y repartimos el espacio entre las 5 casillas
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val spacing = 4.dp * (BOARD_SIZE - 1)   // espacio entre casillas
        val extra = 6.dp * BOARD_SIZE           // borde y relleno de cada casilla
        val cellSize = (maxWidth - spacing - extra) / BOARD_SIZE

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (row in 0 until BOARD_SIZE) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (col in 0 until BOARD_SIZE) {
                        val index = row * BOARD_SIZE + col
                        val territory = board[index]

                        var borderColor = Color.Transparent
                        if (selected.contains(index)) {
                            borderColor = Color.White
                        }

                        Box(
                            modifier = Modifier
                                .border(3.dp, borderColor, RoundedCornerShape(8.dp))
                                .padding(3.dp)
                        ) {
                            TerritoryView(
                                territory = territory,
                                ownerColor = getOwnerColor(territory, players),
                                cellSize = cellSize,
                                onClick = { onTerritoryClick(territory) }
                            )
                        }
                    }
                }
            }
        }
    }
}

fun playerColor(index: Int): Color {
    if (index == 0) {
        return PlayerRed
    }
    if (index == 1) {
        return PlayerBlue
    }
    if (index == 2) {
        return PlayerGreen
    }
    return PlayerYellow
}

private fun getOwnerColor(territory: Territory, players: List<Player>): Color? {
    if (!territory.isControlled()) {
        return null
    }
    for (i in players.indices) {
        if (players[i].id == territory.ownerId) {
            return playerColor(i)
        }
    }
    return null
}