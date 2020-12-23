package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Circle
import com.codingame.gameengine.module.entities.Sprite
import com.codingame.gameengine.module.entities.Text

data class Move(val tileId: Int, val rotation: Int, val tileConnections: IntArray)

fun getBoardPosition(boardIndex: Int): BoardPosition {
    val row: Int
    val index: Int
    val col: Int

    when(boardIndex) {
        0 -> { col = 0; row = 0; index = 0;  }
        1 -> { col = 0; row = 0; index = 1; }
        2 -> { col = 1; row = 0; index = 0; }
        3 -> { col = 1; row = 0; index = 1; }
        4 -> { col = 2; row = 0; index = 0; }
        5 -> { col = 2; row = 0; index = 1; }
        6 -> { col = 3; row = 0; index = 0; }
        7 -> { col = 3; row = 0; index = 1; }
        8 -> { col = 4; row = 0; index = 0; }
        9 -> { col = 4; row = 0; index = 1; }
        10 -> { col = 5; row = 0; index = 0; }
        11 -> { col = 5; row = 0; index = 1; }
        12 -> { col = 5; row = 0; index = 2; }
        13 -> { col = 5; row = 0; index = 3; }

        14 -> { col = 5; row = 1; index = 2; }
        15 -> { col = 5; row = 1; index = 3; }
        16 -> { col = 5; row = 2; index = 2; }
        17 -> { col = 5; row = 2; index = 3; }
        18 -> { col = 5; row = 3; index = 2; }
        19 -> { col = 5; row = 3; index = 3; }
        20 -> { col = 5; row = 4; index = 2; }
        21 -> { col = 5; row = 4; index = 3; }
        22 -> { col = 5; row = 5; index = 2; }
        23 -> { col = 5; row = 5; index = 3; }
        24 -> { col = 5; row = 5; index = 4; }
        25 -> { col = 5; row = 5; index = 5; }

        26 -> { col = 4; row = 5; index = 4; }
        27 -> { col = 4; row = 5; index = 5; }
        28 -> { col = 3; row = 5; index = 4; }
        29 -> { col = 3; row = 5; index = 5; }
        30 -> { col = 2; row = 5; index = 4; }
        31 -> { col = 2; row = 5; index = 5; }
        32 -> { col = 1; row = 5; index = 4; }
        33 -> { col = 1; row = 5; index = 5; }
        34 -> { col = 0; row = 5; index = 4; }
        35 -> { col = 0; row = 5; index = 5; }
        36 -> { col = 0; row = 5; index = 6; }
        37 -> { col = 0; row = 5; index = 7; }

        38 -> { col = 0; row = 4; index = 6; }
        39 -> { col = 0; row = 4; index = 7; }
        40 -> { col = 0; row = 3; index = 6; }
        41 -> { col = 0; row = 3; index = 7; }
        42 -> { col = 0; row = 2; index = 6; }
        43 -> { col = 0; row = 2; index = 7; }
        44 -> { col = 0; row = 1; index = 6; }
        45 -> { col = 0; row = 1; index = 7; }
        46 -> { col = 0; row = 0; index = 6; }
        47 -> { col = 0; row = 0; index = 7; }
        else -> throw IllegalStateException()
    }

    return BoardPosition(col, row, index)
}

data class BoardPosition(val col: Int, val row: Int, val index: Int) {
    override fun toString() = "$col $row $index"
}

class Player : AbstractMultiplayerPlayer() {

    var hand = mutableSetOf<Tile>()
    var lastMove = Move(-1, -1, IntArray(8) {-1} )
    var position = BoardPosition(-1, -1, -1)
    var startingPositionBoardIndex = -1
    var token: Circle? = null
    var message: Text? = null
    val handSprites: Array<Pair<Int, Sprite>?> = Array(3) { null }

    override fun getExpectedOutputLines() = 1

    @Throws(TimeoutException::class, NumberFormatException::class)
    fun getAction(): Action {
        val output = outputs[0].split(" ").toTypedArray()
        return Action(this, output[0].toInt(), output[1].toInt())
    }
}