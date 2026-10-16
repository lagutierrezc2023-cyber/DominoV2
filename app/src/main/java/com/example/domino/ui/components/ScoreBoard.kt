package com.example.domino.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domino.data.model.Player

@Composable
fun ScoreBoard(
    players: List<Player>,
    currentTurn: Int,
    round: Int,
    maxRounds: Int
) {
    var turnName = ""
    if (currentTurn in players.indices) {
        turnName = players[currentTurn].name
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Turno de: " + turnName,
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = "Ronda " + round + " de " + maxRounds)

        for (i in players.indices) {
            val player = players[i]

            var rowColor = Color.Transparent
            var fontWeight = FontWeight.Normal
            if (i == currentTurn) {
                rowColor = Color(0x33FFFFFF)
                fontWeight = FontWeight.Bold
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowColor, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(playerColor(i), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = player.name,
                    fontWeight = fontWeight,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = player.score.toString() + " pts | " +
                            player.territories.toString() + " terr.",
                    fontWeight = fontWeight
                )
            }
        }
    }
}