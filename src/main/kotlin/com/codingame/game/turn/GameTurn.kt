package com.codingame.game.turn

import com.codingame.game.*
import com.codingame.gameengine.core.AbstractPlayer

data class Piece(val tile: Tile)

val board = Array<Array<Piece?>>(6) { Array(6) { null } }

fun Referee.movingTurns(playerId: Int) {
    val activePlayer = gameManager.getPlayer(playerId)

    sendInputsToPlayer(activePlayer)

    try {
        activePlayer.execute()

        // validate input format and data types
        val input = activePlayer.outputs[0].split(" ")
        if (input.size != 3 || input[0] != "PLACE" || input.subList(1, input.size).any { it.toIntOrNull() == null }) {
            kill(activePlayer, 0, String.format("$%d Expected input was 'PLACE <tileId> <tileRotation>", activePlayer.index))
            return
        }
        val tilePick = Move(input[1].toInt(), input[2].toInt())

        // validate input values (check whether card is in player's hand)
        if (activePlayer.hand.none { it.id == tilePick.tileId } || tilePick.rotation < 0 || tilePick.rotation > 3) {
            kill(activePlayer, 0, String.format("$%d Invalid input - either tileId or rotation was invalid", activePlayer.index))
            return
        }

        // place tile based on player pick
        placeTile(tilePick, activePlayer.position)
        board[activePlayer.position.col][activePlayer.position.row] = Piece(tiles[tilePick.tileId].rotated(tilePick.rotation))

        // do moves
        gameManager.players.filter { it.position.col == activePlayer.position.col && it.position.row == activePlayer.position.row }
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
                    }

                    player.position = BoardPosition(actCol, actRow, actualIndex)
                    placePlayer(player, player.position)

                    if (actCol < 0 || actRow < 0 || actCol > 5 || actRow > 5) {
                        // active player commits suicide -> score penalization
                        player.score = actualTurn + if (player == activePlayer) -1 else 0
                        kill(player, player.score, String.format("$%d leaves the board", player.index))
                    }
                }

        if (activePlayer.isActive) {
            // draw from deck if player is still active
            activePlayer.lastMove = tilePick
            activePlayer.hand.removeIf { it.id == tilePick.tileId }
            drawFromDeck(activePlayer)
        }
    } catch (e: AbstractPlayer.TimeoutException) {
        kill(activePlayer, 0, String.format("$%d timeout!", activePlayer.index))
    }
}