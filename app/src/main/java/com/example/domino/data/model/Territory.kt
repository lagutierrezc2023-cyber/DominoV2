package com.example.domino.data.model


class Territory(
    var row: Int = 0,
    var col: Int = 0,
    var effect: TileEffect = TileEffect.NONE,
    var ownerId: String = "",
    var conquests: Int = 0,
    var number: Int = -1
) {


    fun isEmpty(): Boolean {
        return number == -1
    }

    fun conquestsNeeded(): Int {
        if (effect == TileEffect.FORTRESS) {
            return 2
        }
        return 1
    }

    fun isControlled(): Boolean {
        return ownerId != ""
    }

    fun conquer(playerId: String, pieceNumber: Int) {
        number = pieceNumber
        conquests = conquests + 1
        if (conquests >= conquestsNeeded()) {
            ownerId = playerId
        }
    }
}