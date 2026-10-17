package com.example.domino.data.model

data class GameState(
    var players: MutableList<Player> = mutableListOf(),
    var board: MutableList<Territory> = mutableListOf(),
    var pool: MutableList<Piece> = mutableListOf(),
    var currentTurn: Int = 0,
    var round: Int = 1,
    val maxRounds: Int = 20,
    var status: String = "WAITING"
) {
    // Casilla en esa fila y columna y null si queda fuera del tablero
    fun getTerritory(row: Int, col: Int): Territory? {
        return board.find { it.row == row && it.col == col }
    }

    fun currentPlayer(): Player {
        return players[currentTurn]
    }
}