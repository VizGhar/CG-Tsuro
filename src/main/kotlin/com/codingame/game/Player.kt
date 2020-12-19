package com.codingame.game

import com.codingame.gameengine.core.AbstractMultiplayerPlayer
import com.codingame.gameengine.module.entities.Group

class Player : AbstractMultiplayerPlayer() {

    var hud: Group? = null
    var hand = mutableSetOf<Tile>()
    var lastMove: Int? = null

    override fun getExpectedOutputLines() = 1

    @Throws(TimeoutException::class, NumberFormatException::class)
    fun getAction(): Action {
        val output = outputs[0].split(" ").toTypedArray()
        return Action(this, output[0].toInt(), output[1].toInt())
    }
}