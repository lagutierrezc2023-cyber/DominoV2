package com.example.domino.data.model

data class Player(
    val id: String = "",
    val name: String = "",
    val hand: List<Piece> = emptyList(),
    val score: Int = 0,
    val territories: Int = 0
)