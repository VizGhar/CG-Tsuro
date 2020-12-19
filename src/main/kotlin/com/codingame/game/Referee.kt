package com.codingame.game

import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject

class Referee : AbstractReferee() {
    @Inject private val gameManager: MultiplayerGameManager<Player>? = null

    @Inject private val graphicEntityModule: GraphicEntityModule? = null

    override fun init() {}

    override fun gameTurn(turn: Int) {
        for (player in gameManager!!.activePlayers) {
            player.sendInputLine("input")
            player.execute()
        }
        for (player in gameManager.activePlayers) {
            try {
                val outputs = player.outputs
                // Check validity of the player output and compute the new game state
            } catch (e: AbstractPlayer.TimeoutException) {
                player.deactivate(String.format("$%d timeout!", player.index))
            }
        }
    }
}