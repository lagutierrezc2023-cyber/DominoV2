package com.example.domino.domain

import com.example.domino.data.model.GameState
import com.example.domino.data.model.Player

class GameEndChecker(private val engine: GameEngine) {

    fun getReason(state: GameState): String {
        if (state.players.isEmpty() || state.board.isEmpty()) {
            return ""
        }
        for (player in state.players) {
            if (player.hand.isEmpty()) {
                return player.name + " se quedó sin fichas"
            }
        }
        if (state.round > state.maxRounds) {
            return "Se llegó al máximo de rondas"
        }
        if (state.pool.isEmpty() && !someoneCanMove(state)) {
            return "Ya nadie puede mover"
        }
        return ""
    }

    fun isGameOver(state: GameState): Boolean {
        return getReason(state) != ""
    }

    fun getWinner(state: GameState): Player? {
        var best: Player? = null
        for (player in state.players) {
            if (best == null || isBetter(player, best)) {
                best = player
            }
        }
        if (best == null) {
            return null
        }
        for (player in state.players) {
            if (player.id != best.id
                && player.score == best.score
                && player.territories == best.territories
            ) {
                return null
            }
        }
        return best
    }

    private fun someoneCanMove(state: GameState): Boolean {
        for (player in state.players) {
            if (engine.hasValidMove(state, player)) {
                return true
            }
        }
        return false
    }

    private fun isBetter(a: Player, b: Player): Boolean {
        if (a.score != b.score) {
            return a.score > b.score
        }
        return a.territories > b.territories
    }
}