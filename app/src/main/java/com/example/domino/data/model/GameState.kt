package com.example.domino.data.model

data class GameState(
    val players: List<Player> = emptyList(),
    val board: List<Territory> = emptyList(),
    val pool: List<Piece> = emptyList(),
    val currentPlayer: Int = 0,
    val round: Int = 1,
    val maxRounds: Int = 20,
    val finished: Boolean = false
)