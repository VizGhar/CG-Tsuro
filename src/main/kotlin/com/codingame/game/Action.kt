package com.codingame.game

data class Action(val player: Player, val pieceId: Int, val rotation: Int) {
    override fun toString() = "$pieceId $rotation"
}