package com.example.domino.data.model

enum class TileEffect(val label: String, val points: Int) {
    NONE("", 0),
    BONUS("⭐ +3", 3),
    TREASURE("💰 +5", 5),
    TRAP("💀 -2", -2),
    FORTRESS("🛡️ x2", 0),
    PORTAL("🌀", 0);


    fun givesPoints(): Boolean {
        return this == BONUS || this == TREASURE || this == TRAP
    }
}