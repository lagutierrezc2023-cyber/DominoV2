package com.example.domino.data.model

data class Piece(
    val left: Int = 0,
    val right: Int = 0
) {

    fun rotate(): Piece {
        return Piece(right, left)
    }
}