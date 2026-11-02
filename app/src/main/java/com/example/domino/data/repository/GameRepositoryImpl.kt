package com.example.domino.data.repository

import com.example.domino.data.model.GameState
import com.example.domino.data.model.Player
import com.example.domino.data.model.Room
import com.example.domino.data.remote.FirebaseAuthService
import com.example.domino.data.remote.FirebaseGameService

class GameRepositoryImpl(
    private val gameService: FirebaseGameService = FirebaseGameService(),
    private val authService: FirebaseAuthService = FirebaseAuthService()
) : GameRepository {

    private val maxPlayers = 4

    override fun createRoom(playerName: String, onResult: (String) -> Unit) {
        val code = makeCode()
        gameService.getRoom(code) { oldRoom ->
            if (oldRoom != null) {
                createRoom(playerName, onResult)
            } else {
                val host = Player(id = authService.getCurrentUserId(), name = playerName)
                val state = GameState(players = mutableListOf(host))
                gameService.saveRoom(Room(code, state)) {
                    onResult(code)
                }
            }
        }
    }

    override fun joinRoom(code: String, playerName: String, onResult: (Boolean) -> Unit) {
        val roomCode = cleanCode(code)
        val myId = authService.getCurrentUserId()
        gameService.getRoom(roomCode) { room ->
            if (room == null) {
                onResult(false)
            } else if (isInside(room.state.players, myId)) {
                // Ya estaba en la sala, lo dejamos volver a entrar.
                onResult(true)
            } else if (room.state.board.isNotEmpty() || room.state.players.size >= maxPlayers) {
                // La partida ya empezó o la sala está llena.
                onResult(false)
            } else {
                val newPlayers = room.state.players.toMutableList()
                newPlayers.add(Player(id = myId, name = playerName))
                gameService.saveState(roomCode, room.state.copy(players = newPlayers)) {
                    onResult(true)
                }
            }
        }
    }

    override fun listenGame(code: String, onChange: (GameState) -> Unit) {
        gameService.listenRoom(cleanCode(code)) { room ->
            onChange(room.state)
        }
    }

    override fun saveGame(code: String, state: GameState) {
        gameService.saveState(cleanCode(code), state)
    }

    override fun leaveGame(code: String, playerId: String) {
        val roomCode = cleanCode(code)
        gameService.stopListening()
        gameService.getRoom(roomCode) { room ->
            if (room != null) {
                val remaining = mutableListOf<Player>()
                for (player in room.state.players) {
                    if (player.id != playerId) {
                        remaining.add(player)
                    }
                }
                if (remaining.isEmpty()) {
                    gameService.deleteRoom(roomCode)
                } else {
                    gameService.saveRoom(Room(roomCode, newWaitingState(remaining)))
                }
            }
        }
    }

    override fun restartGame(code: String) {
        val roomCode = cleanCode(code)
        gameService.getRoom(roomCode) { room ->
            if (room != null && room.state.board.isNotEmpty()) {
                gameService.saveRoom(Room(roomCode, newWaitingState(room.state.players)))
            }
        }
    }

    private fun makeCode(): String {
        val letters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        var code = ""
        for (i in 1..5) {
            code = code + letters.random()
        }
        return code
    }

    private fun cleanCode(code: String): String {
        return code.trim().uppercase()
    }

    private fun isInside(players: List<Player>, playerId: String): Boolean {
        for (player in players) {
            if (player.id == playerId) {
                return true
            }
        }
        return false
    }

    private fun newWaitingState(players: List<Player>): GameState {
        val freshPlayers = mutableListOf<Player>()
        for (player in players) {
            freshPlayers.add(Player(id = player.id, name = player.name))
        }
        return GameState(players = freshPlayers)
    }
}