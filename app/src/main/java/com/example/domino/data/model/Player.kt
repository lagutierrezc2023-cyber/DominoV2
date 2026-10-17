package com.example.domino.data.model

data class Player(
    val id: String = "",
    val name: String = "",
    var hand: MutableList<Piece> = mutableListOf(),
    var score: Int = 0,
    var territories: Int = 0
)