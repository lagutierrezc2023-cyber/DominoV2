package com.example.dominoconquest.data.repository

import com.example.dominoconquest.data.model.GameState


interface GameRepository {

    fun createRoom(playerName: String, onResult: (String) -> Unit)

    fun joinRoom(code: String, playerName: String, onResult: (Boolean) -> Unit)

    fun listenGame(code: String, onChange: (GameState) -> Unit)

    fun saveGame(code: String, state: GameState)

    fun leaveGame(code: String, playerId: String)

    fun restartGame(code: String)
}