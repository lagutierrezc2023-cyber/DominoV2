package com.example.dominoconquest.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var errorMessage by mutableStateOf("")

    var isLoading by mutableStateOf(false)

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, escribe tu correo electrónico y contraseña"
            return
        }
        errorMessage = ""
        isLoading = true
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage = "Correo electrónico o contraseña incorrectos"
                }
            }
    }

    fun register(onSuccess: () -> Unit) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            return
        }
        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }
        errorMessage = ""
        isLoading = true
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val changes = UserProfileChangeRequest.Builder()
                        .setDisplayName(username.trim())
                        .build()
                    user?.updateProfile(changes)?.addOnCompleteListener {
                        isLoading = false
                        onSuccess()
                    }
                } else {
                    isLoading = false
                    errorMessage = "No se pudo crear la cuenta"
                }
            }
    }

    fun clearFields() {
        username = ""
        email = ""
        password = ""
        confirmPassword = ""
        errorMessage = ""
    }
}