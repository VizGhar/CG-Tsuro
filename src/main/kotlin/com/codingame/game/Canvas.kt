package com.codingame.game

import com.codingame.gameengine.module.entities.Curve
import com.codingame.gameengine.module.entities.Sprite

private const val boardSize = tileSize * 6
private const val tokenSize = tileSize / 6

fun Referee.placePlayer(player: Player, position: BoardPosition) {
    val relativePos = indexToRelativePosition(position.index)
    val x = (1920 - boardSize) / 2 + position.col * tileSize + relativePos.x
    val y = (1080 - boardSize) / 2 + position.row * tileSize + relativePos.y

    if (player.token == null) player.token = graphicEntityModule
            .createCircle()
            .setFillColor(player.colorToken)
            .setLineWidth(0.0)
            .setRadius(tokenSize)

    player.token?.setX(x, Curve.EASE_IN_AND_OUT)?.setY(y, Curve.EASE_IN_AND_OUT)
}

fun Referee.placeTile(move: Move, position: BoardPosition) {

    tileSprites[position.col][position.row]
            ?.setImage("tile${move.tileId.toString().padStart(2, '0')}.png")
            ?.setRotation(Math.PI / 2 * move.rotation, Curve.IMMEDIATE)

}

private var tileSprites: Array<Array<Sprite?>> = Array(6) { Array(6) { null } }

fun Referee.boardFrame() {
    graphicEntityModule.createSprite()
            .setImage("background.jpg")
            .setAnchor(0.0)
            .setZIndex(-100)

    graphicEntityModule
            .createRectangle()
            .setWidth(boardSize + 10)
            .setHeight(boardSize + 10)
            .setX((1920 - boardSize - 5) / 2)
            .setY((1080 - boardSize - 5) / 2)
            .setZIndex(-20)
            .setLineWidth(10.0)
            .setLineColor(0xFFFFFF)
            .setFillColor(0x000000)

    for (row in 0 until 6){
        for(col in 0 until 6) {

            val x = (1920 - boardSize) / 2 + col * tileSize + tileSize / 2
            val y = (1080 - boardSize) / 2 + row * tileSize + tileSize / 2

            val sprite = graphicEntityModule
                    .createSprite()
                    .setAnchorX(0.5)
                    .setAnchorY(0.5)
                    .setZIndex(-10)
                    .setX(x)
                    .setY(y)

            tileSprites[col][row] = sprite
        }
    }
}

fun Referee.hud() {
    for (player in gameManager.players) {
        val x = if (player.index % 2 == 0) 280 else 1920 - 280  // first and third player on left
        val y = if (player.index / 2 == 0) 220 else 1080 - 220  // first and second player on top

        graphicEntityModule
                .createRectangle()
                .setWidth(140)
                .setHeight(140)
                .setX(x - 70)
                .setY(y - 70)
                .setLineWidth(0.0)
                .setFillColor(player.colorToken)

        graphicEntityModule
                .createRectangle()
                .setWidth(120)
                .setHeight(120)
                .setX(x - 60)
                .setY(y - 60)
                .setLineWidth(0.0)
                .setFillColor(0xffffff)

        graphicEntityModule.createText(player.nicknameToken)
                .setX(x)
                .setY(y + 120)
                .setZIndex(20)
                .setFontSize(40)
                .setFillColor(0xffffff)
                .setAnchor(0.5)

        graphicEntityModule.createSprite()
                .setX(x)
                .setY(y)
                .setZIndex(20)
                .setImage(player.avatarToken)
                .setAnchor(0.5)
                .setBaseHeight(116)
                .setBaseWidth(116)

    }
}