package com.example.domino.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domino.data.model.Piece

@Composable
fun PieceView(piece: Piece, isSelected: Boolean = false, onClick: () -> Unit = {}) {
    var borderColor = Color.DarkGray
    if (isSelected) {
        borderColor = Color(0xFF4CAF50)
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFF5E6C8), RoundedCornerShape(6.dp))
            .border(3.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable { onClick() }
    ) {
        HalfPieceView(piece.left)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.DarkGray)
        )
        HalfPieceView(piece.right)
    }
}

@Composable
fun HalfPieceView(number: Int) {
    Canvas(modifier = Modifier.size(40.dp)) {
        val w = size.width
        val radius = w / 9

        // Las 3 posiciones en sentido horizontal y vertical.
        val near = w * 0.25f
        val middle = w * 0.5f
        val far = w * 0.75f

        // Punto central: números 1, 3 y 5.
        if (number == 1 || number == 3 || number == 5) {
            drawCircle(Color.Black, radius, Offset(middle, middle))
        }
        // Esquina superior izquierda e inferior derecha: números 2 o mayores.
        if (number >= 2) {
            drawCircle(Color.Black, radius, Offset(near, near))
            drawCircle(Color.Black, radius, Offset(far, far))
        }
        // Esquina superior derecha e inferior izquierda: números 4 o mayores.
        if (number >= 4) {
            drawCircle(Color.Black, radius, Offset(far, near))
            drawCircle(Color.Black, radius, Offset(near, far))
        }
        // Centro izquierdo y centro derecho: únicamente para el número 6.
        if (number == 6) {
            drawCircle(Color.Black, radius, Offset(near, middle))
            drawCircle(Color.Black, radius, Offset(far, middle))
        }
    }
}