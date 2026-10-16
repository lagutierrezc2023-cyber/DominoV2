package com.example.domino.domain

import com.example.domino.data.model.Territory

class ScoreCalculator {

    fun calculateScore(territories: List<Territory>, playerId: String): Int {
        var total = 0
        for (territory in territories) {
            if (territory.ownerId == playerId) {
                total = total + territory.effect.points
            }
        }
        return total
    }

    fun calculateAllScores(territories: List<Territory>, playerIds: List<String>): Map<String, Int> {
        val scores = mutableMapOf<String, Int>()
        for (playerId in playerIds) {
            scores[playerId] = calculateScore(territories, playerId)
        }
        return scores
    }

    fun countTerritories(territories: List<Territory>, playerId: String): Int {
        var count = 0
        for (territory in territories) {
            if (territory.ownerId == playerId) {
                count = count + 1
            }
        }
        return count
    }

    fun getWinner(territories: List<Territory>, playerIds: List<String>): String {
        val scores = calculateAllScores(territories, playerIds)

        var winner = ""
        var bestScore = Int.MIN_VALUE
        var bestTerritories = -1

        for (playerId in playerIds) {
            val score = scores[playerId] ?: 0
            val territoryCount = countTerritories(territories, playerId)

            if (score > bestScore || (score == bestScore && territoryCount > bestTerritories)) {
                winner = playerId
                bestScore = score
                bestTerritories = territoryCount
            }
        }

        return winner
    }
}