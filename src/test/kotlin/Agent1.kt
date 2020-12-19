import java.util.*

object Agent1 {

    fun pickPosition() {

    }
    @JvmStatic
    fun main(args: Array<String>?) {
        val scanner = Scanner(System.`in`)
        var firstMove = true
        while (true) {

            if (firstMove) {
                pickPosition()
                firstMove = false

                println("0 0 ${listOf(0, 1, 6, 7).random()}")
            } else {

                val opponentCount = scanner.nextLine().toInt()
                for (i in 0 until opponentCount) {
                    val lastOpponentMove = scanner.nextLine()
                }
                val handSize = scanner.nextLine().toInt()
                val cards = (0 until handSize).map {
                    scanner.nextLine().split(" ")
                }

                System.err.println("OpponentCount = $opponentCount")
                System.err.println("My Cards = $cards")
                println("${cards[0][0]} 1")
            }
        }
    }
}