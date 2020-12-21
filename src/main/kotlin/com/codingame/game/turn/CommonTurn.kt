package com.codingame.game.turn

import com.codingame.game.Player
import com.codingame.game.Referee
import com.codingame.game.connections
import com.codingame.game.id

/**
 * These are inputs send to [Player]'s input stream:
 *
 * 1. <opponents> lines containing
 *      1. <tileId> - last tile ID played by opponent
 *      2. <tileRotation> - last tile ROTATION played by opponent
 *      3. <col> - column on which opponent is located
 *      4. <row> - row on which opponent is located
 *      5. <index> - position index (0,1 top; 2,3 right...)
 * 2. <cardCount> - count of cards on my hand
 * 3. <cardCount> lines of
 *      1. <tileId> - identifier of tile
 *      2. <connections> - 4 pairs of tile indexes to connect
 */
fun Referee.sendInputsToPlayer(player: Player) {

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
}

fun Referee.kill(
        player: Player,
        score: Int,
        message: String) {
    player.score = score
    player.deactivate(message)
    deck.addAll(player.hand)
    player.hand.clear()
    deck.shuffle()
}