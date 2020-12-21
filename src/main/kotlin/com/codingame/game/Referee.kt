package com.codingame.game

import com.codingame.game.turn.movingTurns
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.endscreen.EndScreenModule
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import java.util.*

class Referee : AbstractReferee() {

    @Inject lateinit var gameManager: MultiplayerGameManager<Player>
    @Inject lateinit var graphicEntityModule: GraphicEntityModule
    @Inject lateinit var endScreenModule: EndScreenModule

    val random by lazy { Random(gameManager.seed) }
    val deck by lazy { tiles.shuffled(random).toMutableList() }
    var activePlayerId = -1
    var actualTurn = -1

    override fun init() {
        gameManager.maxTurns = 35 + gameManager.playerCount
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
        actualTurn = turn
        do {
            activePlayerId = (activePlayerId + 1) % gameManager.playerCount
        } while (!gameManager.getPlayer(activePlayerId).isActive)

        if (turn <= gameManager.playerCount) {
            doFirstTurn(activePlayerId)
        } else {
            movingTurns(activePlayerId)
        }

        if (gameManager.activePlayers.size < 2){
            gameManager.endGame()
        }
    }

    override fun onEnd() {
        val scores = gameManager.players.map { p: Player -> p.score }.toIntArray()
        endScreenModule.setScores(scores)
    }
}