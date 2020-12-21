package com.codingame.game

import com.codingame.game.turn.movingTurns
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import java.util.*

class Referee : AbstractReferee() {

    @Inject lateinit var gameManager: MultiplayerGameManager<Player>
    @Inject lateinit var graphicEntityModule: GraphicEntityModule

    val random by lazy { Random(gameManager.seed) }
    val deck by lazy { tiles.shuffled(random).toMutableList() }
    var activePlayerId = -1

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
        do {
            activePlayerId = (activePlayerId + 1) % gameManager.playerCount
        } while (!gameManager.getPlayer(activePlayerId).isActive)

        if (turn <= gameManager.playerCount) {
            doFirstTurn(activePlayerId)
        } else {
            movingTurns(activePlayerId)
        }
    }
}