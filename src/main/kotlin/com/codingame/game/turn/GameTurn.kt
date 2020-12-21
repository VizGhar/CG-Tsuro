package com.codingame.game.turn

import com.codingame.game.*
import com.codingame.gameengine.core.AbstractPlayer

data class Piece(val tile: Tile)

val board = Array<Array<Piece?>>(6) { Array(6) { null } }

fun Referee.movingTurns(playerId: Int) {
    val player = gameManager.getPlayer(playerId)

    try {
        sendInputsToPlayer(player)

        // TODO: validate outputs
        val lastMove = player.outputs[0].split(" ").map { it.toInt() }.let {
            Move(it[0], it[1])
        }

        placeTile(lastMove, player.position)
        board[player.position.col][player.position.row] = Piece(tiles[lastMove.tileId].rotated(lastMove.rotation))
        player.lastMove = lastMove
        player.hand.removeIf { it.id == lastMove.tileId }
        if (deck.isNotEmpty()) {
            player.hand.add(deck.removeFirst())
        }

        gameManager.players.filter { it.position.col == player.position.col && it.position.row == player.position.row }
                .forEach { player ->
                    var actCol = player.position.col
                    var actRow = player.position.row
                    var actualIndex = player.position.index
                    while (actCol >= 0 && actRow >= 0 && actCol < 6 && actRow < 6 && board[actCol][actRow] != null) {
                        val actualPiece = board[actCol][actRow]
                        val target = actualPiece!!.tile.connections.toList()
                                .chunked(2)
                                .first { it.contains(actualIndex) }
                                .first { it != actualIndex }
                        System.err.println("Moving $playerId from $actCol : $actRow : $actualIndex : $target")

                        when (target) {
                            0 -> { actRow--; actualIndex = 5;}
                            1 -> { actRow--; actualIndex = 4;}
                            2 -> { actCol++; actualIndex = 7;}
                            3 -> { actCol++; actualIndex = 6;}
                            4 -> { actRow++; actualIndex = 1;}
                            5 -> { actRow++; actualIndex = 0;}
                            6 -> { actCol--; actualIndex = 3;}
                            7 -> { actCol--; actualIndex = 2;}
                        }

                        System.err.println("to $actCol : $actRow : $actualIndex")
                    }
                    if (actCol<0 || actRow<0 || actCol>5 || actRow>5) {
                        player.score = -2
                        player.deactivate(String.format("$%d leaves the board", player.index))
                    } else {
                        player.position = BoardPosition(actCol, actRow, actualIndex)
                        placePlayer(player, player.position)
                    }
                }

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