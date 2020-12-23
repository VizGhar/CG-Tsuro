package com.codingame.game

import com.codingame.gameengine.module.entities.Curve

private const val boardSize = tileSize * 6
private const val tokenSize = tileSize / 6

fun Referee.placePlayer(player: Player, position: BoardPosition, immidiate: Boolean) {
    val relativePos = indexToRelativePosition(position.index)
    val x = (1920 - boardSize) / 2 + position.col * tileSize + relativePos.x
    val y = (1080 - boardSize) / 2 + position.row * tileSize + relativePos.y

    if (player.token == null) player.token = graphicEntityModule
            .createCircle()
            .setFillColor(player.colorToken)
            .setLineWidth(0.0)
            .setRadius(tokenSize)

    if (!immidiate) {
        graphicEntityModule.commitEntityState(0.5, player.token)
    }

    player.token?.setX(x)?.setY(y)
    graphicEntityModule.commitEntityState(if(immidiate) 0.0 else 1.0, player.token)
}

data class MoveMetaData(val col: Int, val row: Int, val index: Int, val distance: Double)

fun Referee.movePlayers(moves: Map<Int, List<MoveMetaData>>) {
    // skip putting tile time
    val skipDistance = 500.0 / gameManager.frameDuration
    var travelledDistance = skipDistance
    moves.forEach { playerId, moves ->
        val player = gameManager.getPlayer(playerId)
        val token = player.token?: throw IllegalStateException()

        graphicEntityModule.commitEntityState(travelledDistance, token)

        moves.forEach { move ->
            val relativePos = indexToRelativePosition(move.index)
            val x = (1920 - boardSize) / 2 + move.col * tileSize + relativePos.x
            val y = (1080 - boardSize) / 2 + move.row * tileSize + relativePos.y

            token.setX(x).setY(y)
            travelledDistance += 2.0 * move.distance / gameManager.frameDuration
            graphicEntityModule.commitEntityState(minOf(travelledDistance, 1.0), token)
        }
    }
}

fun Referee.hidePlayer(player: Player) {
    player.token?.let {
        it.setAlpha(0.0, Curve.EASE_OUT)
        graphicEntityModule.commitEntityState(1.0, it)
    }
    for (i in 0 until player.handSprites.size) {
        player.handSprites[i]?.second?.isVisible = false
        player.handSprites[i] = null
    }
}

fun Referee.dealTile(player: Player, tile: Tile) {
    val horizontalSpace = (1920 - boardSize) / 2
    val verticalSpace = 1080 / ((gameManager.playerCount + 1) / 2)

    val horizontalCenterLeft = horizontalSpace / 2
    val horizontalCenterRight = boardSize + horizontalSpace + horizontalSpace / 2

    val fontSize = if (gameManager.playerCount < 5) 40 else 30
    val cardSize = if (gameManager.playerCount < 5) 120 else 80

    val row = player.index / 2
    val verticalCenter = (row * verticalSpace + (row + 1) * verticalSpace) / 2
    val x = (if (player.index % 2 == 0) horizontalCenterLeft - cardSize else horizontalCenterRight + cardSize)
    val y = verticalCenter - fontSize

    val m = if (player.index % 2 == 0) 1 else -1
    val index = player.handSprites.indexOfFirst { it == null }
    val pos = when(index) {
        -1 -> return
        0 -> x + (2 * cardSize * m) to y - cardSize
        1 -> x + (2 * cardSize * m) to y + 5
        2 -> x + (2 * cardSize * m) to y + cardSize + 10
        else -> throw IllegalStateException()
    }
    val sprite = graphicEntityModule
            .createSprite()
            .setAnchorX(0.5)
            .setAnchorY(0.5)
            .setZIndex(-10)
            .setBaseWidth(cardSize)
            .setBaseHeight(cardSize)
            .setX(pos.x)
            .setY(pos.y)
            .setImage("tile${tile.id.toString().padStart(2, '0')}.png")
    player.handSprites[index] = tile.id to sprite

    graphicEntityModule.commitEntityState(0.5, sprite)
}

fun Referee.placeTile(player: Player, move: Move, col: Int, row: Int) {
    player.handSprites.firstOrNull { it?.first == move.tileId }?.let { sprite ->
        val x = (1920 - boardSize) / 2 + col * tileSize + tileSize / 2
        val y = (1080 - boardSize) / 2 + row * tileSize + tileSize / 2

        sprite.second.setRotation(Math.PI / 2 * move.rotation)
                .setX(x)
                .setY(y)
                .setBaseWidth(150)
                .setBaseHeight(150)

        graphicEntityModule.commitEntityState(500.0 / gameManager.frameDuration, sprite.second)
    }
}

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
    val cardSize = if (gameManager.playerCount < 5) 120 else 80

    for (player in gameManager.players) {
        val row = player.index / 2
        val verticalCenter = (row * verticalSpace + (row + 1) * verticalSpace) / 2
        val x = (if (player.index % 2 == 0) horizontalCenterLeft - cardSize else horizontalCenterRight + cardSize)
        val y = verticalCenter - fontSize

        player.hand.forEach { tile ->
            dealTile(player, tile)
        }

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