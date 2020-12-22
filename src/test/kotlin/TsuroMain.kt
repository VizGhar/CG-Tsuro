import com.codingame.gameengine.runner.MultiplayerGameRunner

object TsuroMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val gameRunner = MultiplayerGameRunner()
        gameRunner.addAgent(Agent1::class.java)
        gameRunner.addAgent(Agent1::class.java)
        gameRunner.addAgent(Agent1::class.java)
        gameRunner.start()
    }
}