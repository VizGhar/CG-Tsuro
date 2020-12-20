package com.codingame.game.turn

import com.codingame.game.*
import com.codingame.gameengine.core.AbstractPlayer

fun Referee.movingTurns(playerId: Int) {
    val player = gameManager.getPlayer(playerId)

    if (!player.isActive) {
        return
    }

    try {
        sendInputsToPlayer(player)

        // TODO: validate outputs
        val lastMove = player.outputs[0].split(" ").map { it.toInt() }.let {
            Move(it[0], it[1])
        }

        player.lastMove = lastMove

        placeTile(lastMove, player.position)
    } catch (e: AbstractPlayer.TimeoutException) {
        player.deactivate(String.format("$%d timeout!", player.index))
    }
}

/**
 * These are inputs send to [Player]'s input stream:
 *
 * 1. <opponents> - opponent count
 * 2. <opponents> lines containing
 *      1. <tileId> - last tile ID played by opponent
 *      2. <tileRotation> - last tile ROTATION played by opponent
 *      3. <col> - column on which opponent is located
 *      4. <row> - row on which opponent is located
 *      5. <index> - position index (0,1 top; 2,3 right...)
 * 3. <cardCount> - count of cards on my hand
 * 4. <cardCount> lines of
 *      1. <tileId> - identifier of tile
 *      2. <connections> - 4 pairs of tile indexes to connect
 */
private fun Referee.sendInputsToPlayer(player: Player) {
    // opponent count
    player.sendInputLine((gameManager.players.size - 1).toString())

    // last opponent moves
    for (i in 1 until gameManager.players.size) {
        val opponent = gameManager.getPlayer((player.index + i) % gameManager.players.size)
        player.sendInputLine("${opponent.lastMove.tileId} ${opponent.lastMove.rotation} ${opponent.position.col} ${opponent.position.row} ${opponent.position.index}")
    }

    // my card count
    player.sendInputLine(player.hand.size.toString())

    // my cards
    for (tile in player.hand) {
        player.sendInputLine("${tile.id} ${tile.connections.joinToString(" ")}")
    }

    player.execute()
}