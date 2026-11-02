package com.example.domino.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.domino.data.repository.GameRepository

class LobbyViewModel(private val repository: GameRepository) : ViewModel() {

    var playerName by mutableStateOf("")
    var roomCode by mutableStateOf("")
    var errorMessage by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun createRoom(onSuccess: (String) -> Unit) {
        if (playerName.isBlank()) {
            errorMessage = "Escribe tu nombre antes de crear la sala"
            return
        }

        errorMessage = ""
        isLoading = true

        repository.createRoom(playerName.trim()) { code ->
            isLoading = false
            roomCode = code
            onSuccess(code)
        }
    }

    fun joinRoom(onSuccess: () -> Unit) {
        if (playerName.isBlank()) {
            errorMessage = "Escribe tu nombre antes de unirte"
            return
        }
        if (roomCode.isBlank()) {
            errorMessage = "Escribe el codigo de la sala"
            return
        }

        errorMessage = ""
        isLoading = true

        repository.joinRoom(roomCode.trim(), playerName.trim()) { success ->
            isLoading = false
            if (success) {
                onSuccess()
            } else {
                errorMessage = "No se pudo encontrar esa sala"
            }
        }
    }

    fun clearFields() {
        playerName = ""
        roomCode = ""
        errorMessage = ""
    }
}