package com.codingame.game

import com.codingame.gameengine.core.AbstractPlayer

/**
 * Validate first turn of each player.
 *
 * If invalid - deactivate player and set his score to -1
 * If valid - store position to [Player.position] and call
 *
 * These are inputs send to [Player]'s input stream:
 * 1. <opponents> - opponent count
 * 2. <opponents> lines containing
 *      1. <col> - column on which opponent is located
 *      2. <row> - row on which opponent is located
 *      3. <index> - position index (0,1 top; 2,3 right...)
 */
fun Referee.doFirstTurn(playerId: Int) {

    val player = gameManager.getPlayer(playerId)
    val opponents = (1 until gameManager.players.size).map {
        gameManager.getPlayer((playerId + it) % gameManager.players.size)
    }

    player.sendInputLine("${gameManager.playerCount - 1}")
    opponents.forEach { opponent ->
        player.sendInputLine(opponent.position.toString())
    }

    try {
        player.execute()

        val pickedPosition = fromOutput(player.outputs[0])

        val valid = when {
            pickedPosition.row == 0 -> {
                when {
                    pickedPosition.col == 0 && pickedPosition.index !in listOf(0, 1, 6, 7) -> false
                    pickedPosition.col == 5 && pickedPosition.index !in listOf(0, 1, 2, 3) -> false
                    pickedPosition.col in 1..4 && pickedPosition.index !in listOf(0, 1) -> false
                    else -> true
                }
            }
            pickedPosition.row == 5 -> {
                when {
                    pickedPosition.col == 0 && pickedPosition.index !in listOf(4, 5, 6, 7) -> false
                    pickedPosition.col == 5 && pickedPosition.index !in listOf(2, 3, 4, 5) -> false
                    pickedPosition.col in 1..4 && pickedPosition.index !in listOf(4, 5) -> false
                    else -> true
                }
            }
            pickedPosition.col == 0 -> {
                when {
                    pickedPosition.index !in listOf(6, 7) -> false
                    else -> true
                }
            }
            pickedPosition.col == 5 -> {
                when {
                    pickedPosition.index !in listOf(2, 3) -> false
                    else -> true
                }
            }
            else -> false
        }

        when {
            valid && pickedPosition !in opponents.map { it.position } -> {
                player.position = pickedPosition
                placePlayer(player, pickedPosition)
            }
            valid -> player.deactivate(String.format("Invalid starting position - same as opponent", player.index))
            else -> player.deactivate(String.format("Invalid starting position", player.index))
        }
    }  catch (e: AbstractPlayer.TimeoutException) {
        System.err.println("Timeout Deactivation 1")
        player.score = -1
        player.deactivate(String.format("$%d timeout!", player.index))
    }
}