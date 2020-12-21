package com.codingame.game.turn

import com.codingame.game.Player
import com.codingame.game.Referee
import com.codingame.game.connections
import com.codingame.game.id
import com.codingame.gameengine.core.GameManager
import com.codingame.gameengine.core.MultiplayerGameManager

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

fun MultiplayerGameManager<Player>.nextActivePlayer(activePlayer: Player): Player? {
    for (i in 1 until players.size) {
        val index = (activePlayer.index) + i % playerCount
        val player = players.first { it.index == index }
        if (player.isActive) {
            return player
        }
    }
    return null
}

private var tsuroIndex = -1

fun Referee.drawFromDeck(player: Player) {
    if (player.isActive) {
        if (deck.isNotEmpty()) {
            player.hand.add(deck.removeFirst())
            if (tsuroIndex != -1) {
                System.err.println("Moving Tsuro Tile to next player (or discarding)")
                val nextActivePlayer = gameManager.nextActivePlayer(player)
                tsuroIndex = when {
                    nextActivePlayer == null -> -1
                    nextActivePlayer.hand.size == 3 -> -1
                    else -> nextActivePlayer.index
                }
            }
        } else if (tsuroIndex == -1) {
            System.err.println("Current player has nothing to draw - receives Tsuro Tile")
            tsuroIndex = player.index
        }
    }
}

fun Referee.kill(
        player: Player,
        score: Int,
        message: String) {
    player.score = score
    gameManager.addToGameSummary(GameManager.formatErrorMessage(message))
    player.deactivate(message)
    deck.addAll(player.hand)
    player.hand.clear()
    deck.shuffle()

    // deal cards - first player who were unable to pick receives first card
    var activeIndex = (player.index + 1) % gameManager.players.size
    while (true) {
        if (gameManager.activePlayers.all { it.hand.size == 3 }) break
        if (deck.isEmpty()) break

        val activePlayer = gameManager.getPlayer(activeIndex)
        drawFromDeck(activePlayer)

        activeIndex = (activeIndex + 1) % gameManager.players.size
    }
}