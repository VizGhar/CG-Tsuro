package com.codingame.game

import com.codingame.game.turn.kill
import com.codingame.game.turn.sendInputsToPlayer
import com.codingame.gameengine.core.AbstractPlayer

/**
 * Validate first turn of each player.
 *
 * If invalid - deactivate player and set his score to 0
 * If valid - store position to [Player.position] and call
 *
 */
fun Referee.doFirstTurn(playerId: Int) {

    val player = gameManager.getPlayer(playerId)
    val opponents = (1 until gameManager.players.size).map {
        gameManager.getPlayer((playerId + it) % gameManager.players.size)
    }

    player.sendInputLine("${gameManager.playerCount}")

    sendInputsToPlayer(player)

    try {
        player.execute()

        val input = player.outputs[0].split(" ")

        // valid items - count & format
        if (input.size < 2 || input[0] != "START" || input[1].toIntOrNull() == null) {
            kill(player, 0, String.format("$%d Expected input was 'START <boardIndex> <optional message>'", player.index))
            return
        }

        // valid items - value
        val boardPosition = input[1].toInt()
        if (boardPosition < 0 || boardPosition > 47) {
            kill(player, 0, String.format("$%d Starting position boardIndex should be picked from interval 0-47", player.index))
            return
        }

        // valid items - respect to game
        val pickedPosition = getBoardPosition(input[1].toInt())
        if (boardPosition in opponents.map { it.startingPositionBoardIndex }) {
            kill(player, 0, String.format("$%d Invalid starting position - same as opponent", player.index))
            return
        }

        // place player on board
        player.position = pickedPosition
        player.startingPositionBoardIndex = boardPosition
        placePlayer(player, pickedPosition, true)
        if (input.size > 2) {
            speak(player, input.subList(2, input.size).joinToString(" "))
        }
    }  catch (e: AbstractPlayer.TimeoutException) {
        kill(player, 0, String.format("$%d timeout!", player.index))
    }
}