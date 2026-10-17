package com.example.domino.domain

import com.example.domino.data.model.GameState

class TurnManager(private val engine: GameEngine) {

    fun isMyTurn(state: GameState, playerId: String): Boolean {
        return state.status == "PLAYING" && state.currentPlayer().id == playerId
    }

    // Debe tomar ficha si es su turno y no tiene ninguna jugada 
    fun mustDraw(state: GameState, playerId: String): Boolean {
        return isMyTurn(state, playerId) && !engine.hasValidMove(state, state.currentPlayer())
    }

    // Toma una ficha, si el pozo está vacío, pierde el turno
    fun drawOrSkip(state: GameState) {
        if (!engine.drawFromPool(state)) {
            engine.endTurn(state)
        }
    }
}