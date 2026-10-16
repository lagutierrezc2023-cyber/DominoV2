package com.example.domino.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dominoconquest.viewModel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit) {

    var isRegister by remember { mutableStateOf(false) }

    var title = "Bienvenido"
    if (isRegister) {
        title = "Crear cuenta"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(16.dp))

        if (isRegister) {
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isRegister) {
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (viewModel.errorMessage != "") {
            Text(text = viewModel.errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (isRegister) {
                    viewModel.register(onLoginSuccess)
                } else {
                    viewModel.login(onLoginSuccess)
                }
            },
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isRegister) {
                Text("Registrarse")
            } else {
                Text("Iniciar sesión")
            }
        }

        TextButton(onClick = {
            isRegister = !isRegister
            viewModel.clearFields()
        }) {
            if (isRegister) {
                Text("¿Ya tienes una cuenta? Inicia sesión")
            } else {
                Text("¿No tienes una cuenta? Regístrate")
            }
        }
    }
}