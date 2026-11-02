package com.example.domino.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.domino.data.model.Piece

@Composable
fun PlayerHand(pieces: List<Piece>, selected: Piece?, onSelect: (Piece) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(pieces) { piece ->
            PieceView(piece, piece == selected) { onSelect(piece) }
        }
    }
}