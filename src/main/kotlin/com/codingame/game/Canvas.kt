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

fun Referee.hidePlayer(player: Player) {
    player.token?.setAlpha(0.0, Curve.EASE_OUT)
}

fun Referee.placeTile(move: Move, position: BoardPosition) {

    tileSprites[position.col][position.row]?.let { tile ->
        tile.setImage("tile${move.tileId.toString().padStart(2, '0')}.png")
                .setRotation(Math.PI / 2 * move.rotation,Curve.EASE_OUT)
        graphicEntityModule.commitEntityState(0.01, tile)

        tile.setAlpha(1.0, Curve.EASE_OUT)

        graphicEntityModule.commitEntityState(0.5, tile)
    }
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
                    .setAlpha(0.0)
                    .setX(x)
                    .setY(y)

            tileSprites[col][row] = sprite
        }
    }
}

fun Referee.hud() {
    val horizontalSpace = (1920 - boardSize) / 2
    val verticalSpace = 1080 / ((gameManager.playerCount + 1) / 2)

    val horizontalCenterLeft = horizontalSpace / 2
    val horizontalCenterRight = boardSize + horizontalSpace + horizontalSpace / 2

    val frameSize = if (gameManager.playerCount < 5) 140 else 100
    val whiteRectangleSize = frameSize - 20
    val avatarSize = whiteRectangleSize - 8
    val fontSize = if (gameManager.playerCount < 5) 40 else 30

    for (player in gameManager.players) {
        val x = if (player.index % 2 == 0) horizontalCenterLeft else horizontalCenterRight  // odd players on left
        val row = player.index / 2
        val verticalCenter = (row * verticalSpace + (row + 1) * verticalSpace) / 2
        val y = verticalCenter - fontSize  // first and second player on top

        graphicEntityModule
                .createRectangle()
                .setWidth(frameSize)
                .setHeight(frameSize)
                .setX(x - frameSize / 2)
                .setY(y - frameSize / 2)
                .setLineWidth(0.0)
                .setFillColor(player.colorToken)

        graphicEntityModule
                .createRectangle()
                .setWidth(whiteRectangleSize)
                .setHeight(whiteRectangleSize)
                .setX(x - whiteRectangleSize / 2)
                .setY(y - whiteRectangleSize / 2)
                .setLineWidth(0.0)
                .setFillColor(0xffffff)

        graphicEntityModule.createSprite()
                .setX(x)
                .setY(y)
                .setZIndex(20)
                .setImage(player.avatarToken)
                .setAnchor(0.5)
                .setBaseHeight(avatarSize)
                .setBaseWidth(avatarSize)

        graphicEntityModule.createText(player.nicknameToken)
                .setX(x)
                .setY(y + frameSize / 2 + fontSize)
                .setZIndex(20)
                .setFontSize(fontSize)
                .setFillColor(0xffffff)
                .setAnchor(0.5)

        player.message = graphicEntityModule.createText()
                .setX(x)
                .setY(y + frameSize / 2 + 2 * fontSize)
                .setZIndex(20)
                .setFontSize(fontSize - 4)
                .setFillColor(0xffffff)
                .setAnchor(0.5)

    }
}

fun Referee.speak(player: Player, message: String) {
    player.message?.let {
        it.setText(message)
        graphicEntityModule.commitEntityState(0.0, it)
    }
}