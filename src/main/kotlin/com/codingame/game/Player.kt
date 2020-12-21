package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Circle
import com.codingame.gameengine.module.entities.Group

data class Move(val tileId: Int, val rotation: Int)

data class BoardPosition(val col: Int, val row: Int, val index: Int) {
    override fun toString() = "$col $row $index"
}

class Player : AbstractMultiplayerPlayer() {

    var hand = mutableSetOf<Tile>()
    var lastMove = Move(-1, -1)
    var position = BoardPosition(-1, -1, -1)
    var token: Circle? = null

    override fun getExpectedOutputLines() = 1

    @Throws(TimeoutException::class, NumberFormatException::class)
    fun getAction(): Action {
        val output = outputs[0].split(" ").toTypedArray()
        return Action(this, output[0].toInt(), output[1].toInt())
    }
}