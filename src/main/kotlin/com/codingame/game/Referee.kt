package com.codingame.game

import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.codingame.gameengine.module.entities.Text
import com.google.inject.Inject
import java.util.*

private const val boardSize = tileSize * 6

class Referee : AbstractReferee() {

    @Inject private lateinit var gameManager: MultiplayerGameManager<Player>
    @Inject private lateinit var graphicEntityModule: GraphicEntityModule

    private val random by lazy { Random(gameManager.seed) }
    private val deck by lazy { tiles.shuffled(random).toMutableList() }

    override fun init() {
        gameManager.maxTurns = 35
        gameManager.firstTurnMaxTime = 1000
        gameManager.turnMaxTime = 50
        gameManager.frameDuration = 600

        // offer tiles
        for (player in gameManager.players) {
            for (i in 0 until 3) {
                player.hand.add(deck.removeFirst())
            }
        }

        // draw screen
        hud()
        boardFrame()
    }

    override fun gameTurn(turn: Int) {
        val player = gameManager.getPlayer(turn % gameManager.playerCount)

        if (player.isActive) {
            player.sendInputLine("input")
            player.execute()
        }

        try {
            val outputs = player.outputs

            val row = (turn - 1) / 6
            val column = (turn - 1) % 6
            val tile = tiles[turn - 1]
            val x = (1920 - boardSize) / 2 + column * tileSize
            val y = (1080 - boardSize) / 2 + row * tileSize

            tile.relativePositions(2).chunked(2) { (from, to) ->
                graphicEntityModule
                        .createLine()
                        .setX(x + from.x)
                        .setY(y + from.y)
                        .setX2(x + to.x)
                        .setY2(y + to.y)
                        .setLineWidth(5.0)
                        .setLineColor(0x333333)
            }

        } catch (e: AbstractPlayer.TimeoutException) {
            player.deactivate(String.format("$%d timeout!", player.index))
        }
    }

    private fun boardFrame() {
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

    private fun hud() {
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
}