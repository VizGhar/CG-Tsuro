import java.util.*

object Agent1 {

    @JvmStatic
    fun main(args: Array<String>?) {
        val scanner = Scanner(System.`in`)
        var firstMove = true
        while (true) {

            if (firstMove) {
                val opponentCount = scanner.nextLine().toInt()
                (0 until opponentCount).map { scanner.nextLine() }
                firstMove = false
                println("0 0 ${listOf(0, 1, 6, 7).random()}")
            } else {

                val opponentCount = scanner.nextLine().toInt()
                (0 until opponentCount).map { scanner.nextLine() }
                val handSize = scanner.nextLine().toInt()
                val cards = (0 until handSize).map { scanner.nextLine().split(" ") }

                println("${cards[0][0]} 1")
            }
        }
    }
}