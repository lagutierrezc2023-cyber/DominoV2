package com.example.domino.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DominoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF2E9E5B),
            background = Color(0xFF0F2A1F),
            surface = Color(0xFF16382A)
        ),
        typography = Typography,
        content = content
    )
}