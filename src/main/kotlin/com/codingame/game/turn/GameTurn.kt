package com.codingame.game.turn

import com.codingame.game.*
import com.codingame.gameengine.core.AbstractPlayer
import kotlin.math.roundToInt

data class Piece(val tile: Tile)

val board = Array<Array<Piece?>>(6) { Array(6) { null } }

fun Referee.movingTurns(playerId: Int) {
    val activePlayer = gameManager.getPlayer(playerId)

    sendInputsToPlayer(activePlayer)

    try {
        activePlayer.execute()

        // validate input format and data types
        val input = activePlayer.outputs[0].split(" ")
        if (input.size < 3 || input[0] != "PLACE" || input.subList(1, 3).any { it.toIntOrNull() == null }) {
            kill(activePlayer, 0, String.format("$%d Expected input was 'PLACE <tileId> <tileRotation> <optional message>", activePlayer.index))
            return
        }

        // validate input values (check whether card is in player's hand)
        val tile = tiles[input[1].toInt()]
        val tilePick = Move(input[1].toInt(), input[2].toInt(), tile.rotated(input[2].toInt()).connections)
        if (activePlayer.hand.none { it.id == tilePick.tileId } || tilePick.rotation < 0 || tilePick.rotation > 3) {
            kill(activePlayer, 0, String.format("$%d Invalid input - either tileId or rotation was invalid", activePlayer.index))
            return
        }

        val placingCol = activePlayer.position.col
        val placingRow = activePlayer.position.row

        // place tile based on player pick
        if (input.size > 3) {
            speak(activePlayer, input.subList(3, input.size).joinToString(" "))
        }

        board[activePlayer.position.col][activePlayer.position.row] = Piece(tiles[tilePick.tileId].rotated(tilePick.rotation))

        val movingPlayers = gameManager.players.filter { it.position.col == activePlayer.position.col && it.position.row == activePlayer.position.row }
        val moves = movingPlayers.map { it.index to mutableListOf<MoveMetaData>() }.toMap()
        movingPlayers.forEach { player ->
                    var actCol = player.position.col
                    var actRow = player.position.row
                    var actualIndex = player.position.index
                    while (actCol >= 0 && actRow >= 0 && actCol < 6 && actRow < 6 && board[actCol][actRow] != null) {
                        val actualPiece = board[actCol][actRow]
                        val from = actualIndex
                        val to = actualPiece!!.tile.connections.toList()
                                .chunked(2)
                                .first { it.contains(actualIndex) }
                                .first { it != actualIndex }

                        when (to) {
                            0 -> { actRow--; actualIndex = 5;}
                            1 -> { actRow--; actualIndex = 4;}
                            2 -> { actCol++; actualIndex = 7;}
                            3 -> { actCol++; actualIndex = 6;}
                            4 -> { actRow++; actualIndex = 1;}
                            5 -> { actRow++; actualIndex = 0;}
                            6 -> { actCol--; actualIndex = 3;}
                            7 -> { actCol--; actualIndex = 2;}
                        }
                        moves[player.index]?.add(MoveMetaData(actCol, actRow, actualIndex, indexDistance(from, to)))
                    }
                    player.position = BoardPosition(actCol, actRow, actualIndex)
                }

        val totalDistance = moves.values.map { it.sumByDouble { it.distance } }.sum()
        gameManager.frameDuration = 500 + 2 * totalDistance.roundToInt()
        placeTile(activePlayer, tilePick, placingCol, placingRow)
        movePlayers(moves)

        for (player in gameManager.activePlayers) {
            if (player.position.col < 0 || player.position.row < 0 || player.position.col > 5 || player.position.row > 5) {
                // active player commits suicide -> score penalization
                player.score = actualTurn + if (player == activePlayer) -1 else 0
                kill(player, player.score, String.format("$%d leaves the board", player.index))
            }
        }

        if (activePlayer.isActive) {
            // draw from deck if player is still active
            activePlayer.lastMove = tilePick
            activePlayer.hand.removeIf { it.id == tilePick.tileId }
            val i = activePlayer.handSprites.indexOfFirst { it?.first == tilePick.tileId }
            if (i != -1) activePlayer.handSprites[i] = null
            drawFromDeck(activePlayer)
        }
    } catch (e: AbstractPlayer.TimeoutException) {
        kill(activePlayer, 0, String.format("$%d timeout!", activePlayer.index))
    }
}