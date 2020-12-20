package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Group

data class Move(val tileId: Int, val rotation: Int)

data class BoardPosition(val col: Int, val row: Int, val index: Int) {
    override fun toString() = "$col $row $index"
}

fun fromOutput(output: String) : BoardPosition {
    val pickedPosition = output.split(" ").map { it.toInt() }
    return BoardPosition(pickedPosition[0], pickedPosition[1], pickedPosition[2])
}

class Player : AbstractMultiplayerPlayer() {

    var hud: Group? = null
    var hand = mutableSetOf<Tile>()
    var lastMove = Move(-1, -1)
    var position = BoardPosition(-1, -1, -1)

    override fun getExpectedOutputLines() = 1

    @Throws(TimeoutException::class, NumberFormatException::class)
    fun getAction(): Action {
        val output = outputs[0].split(" ").toTypedArray()
        return Action(this, output[0].toInt(), output[1].toInt())
    }
}