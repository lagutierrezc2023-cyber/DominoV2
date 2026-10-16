package com.example.domino.domain

import com.example.domino.data.model.GameState
import com.example.domino.data.model.Piece
import com.example.domino.data.model.Player
import com.example.domino.data.model.Territory
import com.example.domino.data.model.TileEffect
import kotlin.math.abs


class GameEngine {

    // Es true cuando el jugador coloca una ficha en un portal
    var extraTurn = false

    private val rowMoves = intArrayOf(-1, 1, 0, 0)
    private val colMoves = intArrayOf(0, 0, -1, 1)

    // Crea las 25 casillas y asigna las casillas especiales.
    fun createBoard(): MutableList<Territory> {
        val board = mutableListOf<Territory>()
        for (row in 0..4) {
            for (col in 0..4) {
                board.add(Territory(row, col))
            }
        }
        // Posición en la lista = fila * 5 + columna.
        board[0].effect = TileEffect.BONUS
        board[24].effect = TileEffect.BONUS
        board[6].effect = TileEffect.TREASURE
        board[18].effect = TileEffect.TREASURE
        board[8].effect = TileEffect.TRAP
        board[16].effect = TileEffect.TRAP
        board[12].effect = TileEffect.FORTRESS
        board[2].effect = TileEffect.PORTAL
        board[22].effect = TileEffect.PORTAL
        return board
    }

    fun createPool(): MutableList<Piece> {
        val pool = mutableListOf<Piece>()
        for (i in 0..6) {
            for (j in i..6) {
                pool.add(Piece(i, j))
            }
        }
        pool.shuffle()
        return pool
    }

    fun startGame(players: MutableList<Player>): GameState {
        val state = GameState()
        state.board = createBoard()
        state.players = players
        state.pool = createPool()
        for (player in players) {
            player.hand = mutableListOf()
            for (i in 1..7) {
                player.hand.add(state.pool.removeAt(0))
            }
        }
        state.status = "PLAYING"
        return state
    }

    fun isAdjacent(row1: Int, col1: Int, row2: Int, col2: Int): Boolean {
        return abs(row1 - row2) + abs(col1 - col2) == 1
    }

    fun boardIsEmpty(state: GameState): Boolean {
        for (t in state.board) {
            if (!t.isEmpty()) {
                return false
            }
        }
        return true
    }

    fun canPlaceOn(t: Territory): Boolean {
        if (t.isEmpty()) {
            return true
        }
        return t.effect == TileEffect.FORTRESS && t.conquests < 2
    }

    private fun touches(state: GameState, row: Int, col: Int, number: Int): Boolean {
        for (i in 0..3) {
            val neighbor = state.getTerritory(row + rowMoves[i], col + colMoves[i])
            if (neighbor != null && !neighbor.isEmpty() && neighbor.number == number) {
                return true
            }
        }
        return false
    }

    fun isValidMove(
        state: GameState, piece: Piece,
        row1: Int, col1: Int, row2: Int, col2: Int
    ): Boolean {
        if (!isAdjacent(row1, col1, row2, col2)) {
            return false
        }
        val first = state.getTerritory(row1, col1)
        val second = state.getTerritory(row2, col2)
        if (first == null || second == null) {
            return false
        }
        if (!canPlaceOn(first) || !canPlaceOn(second)) {
            return false
        }
        if (boardIsEmpty(state)) {
            return true
        }
        return touches(state, row1, col1, piece.left) || touches(state, row2, col2, piece.right)
    }

    private fun conquerSquare(player: Player, t: Territory, number: Int) {
        t.conquer(player.id, number)
        if (t.conquests == t.conquestsNeeded()) {
            player.score = player.score + 1 + t.effect.points
            player.territories = player.territories + 1
        }
        if (t.effect == TileEffect.PORTAL) {
            extraTurn = true
        }
    }

    fun placePiece(
        state: GameState, piece: Piece,
        row1: Int, col1: Int, row2: Int, col2: Int
    ): Boolean {
        if (!isValidMove(state, piece, row1, col1, row2, col2)) {
            return false
        }
        val player = state.currentPlayer()
        val first = state.getTerritory(row1, col1)!!
        val second = state.getTerritory(row2, col2)!!
        conquerSquare(player, first, piece.left)
        conquerSquare(player, second, piece.right)
        player.hand.remove(piece)
        return true
    }

    fun drawFromPool(state: GameState): Boolean {
        if (state.pool.isEmpty()) {
            return false
        }
        val piece = state.pool.removeAt(0)
        state.currentPlayer().hand.add(piece)
        return true
    }

    fun hasValidMove(state: GameState, player: Player): Boolean {
        for (piece in player.hand) {
            for (t in state.board) {
                for (i in 0..3) {
                    if (isValidMove(state, piece, t.row, t.col, t.row + rowMoves[i], t.col + colMoves[i])) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun endTurn(state: GameState) {
        if (extraTurn) {
            extraTurn = false
        } else {
            state.currentTurn = state.currentTurn + 1
            if (state.currentTurn >= state.players.size) {
                state.currentTurn = 0
                state.round = state.round + 1
            }
        }
        checkGameOver(state)
    }

    fun checkGameOver(state: GameState): Boolean {
        var finished = false
        for (player in state.players) {
            if (player.hand.isEmpty()) {
                finished = true
            }
        }
        if (state.round > state.maxRounds) {
            finished = true
        }
        var someoneCanMove = false
        for (player in state.players) {
            if (hasValidMove(state, player)) {
                someoneCanMove = true
            }
        }
        if (!someoneCanMove && state.pool.isEmpty()) {
            finished = true
        }
        if (finished) {
            state.status = "FINISHED"
        }
        return finished
    }

    fun getWinner(state: GameState): Player? {
        var best: Player? = null
        var tie = false
        for (player in state.players) {
            if (best == null) {
                best = player
            } else if (player.score > best.score) {
                best = player
                tie = false
            } else if (player.score == best.score) {
                if (player.territories > best.territories) {
                    best = player
                    tie = false
                } else if (player.territories == best.territories) {
                    tie = true
                }
            }
        }
        if (tie) {
            return null
        }
        return best
    }
}