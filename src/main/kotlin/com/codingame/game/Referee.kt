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

    private val random by lazy { Random(gameManager.seed) }
    private val deck by lazy { tiles.shuffled(random).toMutableList() }

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
        val playerId = turn % gameManager.playerCount   // TODO: removed players?

        if (turn <= gameManager.playerCount) {
            doFirstTurn(playerId)
        } else {
            movingTurns(playerId)
        }
    }
}