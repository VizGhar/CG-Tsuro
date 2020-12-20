package com.codingame.game

import com.codingame.gameengine.module.entities.Text

private const val boardSize = tileSize * 6
private const val tokenSize = tileSize / 10

fun Referee.placePlayer(player: Player, position: BoardPosition) {
    val relativePos = indexToRelativePosition(position.index)
    val x = (1920 - boardSize) / 2 + position.col * tileSize + relativePos.x
    val y = (1080 - boardSize) / 2 + position.row * tileSize + relativePos.y

    graphicEntityModule
            .createCircle()
            .setFillColor(player.colorToken)
            .setLineWidth(0.0)
            .setX(x)
            .setY(y)
            .setRadius(tokenSize)
}

fun Referee.placeTile(move: Move, position: BoardPosition) {
    val tile = tiles[move.tileId - 1]
    val x = (1920 - boardSize) / 2 + position.col * tileSize
    val y = (1080 - boardSize) / 2 + position.row * tileSize

    tile.relativePositions(move.rotation).chunked(2) { (from, to) ->
        graphicEntityModule
                .createLine()
                .setX(x + from.x)
                .setY(y + from.y)
                .setX2(x + to.x)
                .setY2(y + to.y)
                .setLineWidth(5.0)
                .setLineColor(0x333333)
    }
}

fun Referee.boardFrame() {
    graphicEntityModule
            .createRectangle()
            .setWidth(boardSize)
            .setHeight(boardSize)
            .setX((1920 - boardSize) / 2)
            .setY((1080 - boardSize) / 2)
            .setLineWidth(10.0)
            .setLineColor(0xFFFFFF)
            .setFillColor(0x000000)
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

        val text: Text = graphicEntityModule.createText(player.nicknameToken)
                .setX(x)
                .setY(y + 120)
                .setZIndex(20)
                .setFontSize(40)
                .setFillColor(0xffffff)
                .setAnchor(0.5)

        val avatar = graphicEntityModule.createSprite()
                .setX(x)
                .setY(y)
                .setZIndex(20)
                .setImage(player.avatarToken)
                .setAnchor(0.5)
                .setBaseHeight(116)
                .setBaseWidth(116)

        player.hud = graphicEntityModule.createGroup(text, avatar)
    }
}