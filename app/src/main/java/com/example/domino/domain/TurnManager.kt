package com.example.domino.domain

import com.example.domino.data.model.GameState

class TurnManager {

    // Pasa al siguiente jugador sube la ronda cuando vuelve al primero
    fun nextTurn(state: GameState): GameState {
        val next = (state.currentPlayer + 1) % state.players.size
        val round = if (next == 0) state.round + 1 else state.round
        return state.copy(currentPlayer = next, round = round)
    }
}