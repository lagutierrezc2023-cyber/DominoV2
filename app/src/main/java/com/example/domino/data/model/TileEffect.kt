package com.example.domino.data.model

enum class TileEffect(val label: String, val points: Int) {
    NONE("No effect", 0),
    BONUS("Bonus", 3),
    TREASURE("Treasure", 5),
    TRAP("Trap", -2),
    FORTRESS("Fortress", 0),
    PORTAL("Portal", 0);


    fun givesPoints(): Boolean {
        return this == BONUS || this == TREASURE || this == TRAP
    }
}