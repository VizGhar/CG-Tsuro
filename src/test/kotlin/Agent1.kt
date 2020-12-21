import java.util.*

object Agent1 {

    @JvmStatic
    fun main(args: Array<String>?) {
        val scanner = Scanner(System.`in`)

        val opponentCount = scanner.nextLine().toInt()
        (0 until opponentCount).map { scanner.nextLine() }

        val position = Random().nextInt(48)
        val row: Int
        val col: Int
        val index: Int
        when(position) {
            0 -> { row = 0; index = 0; col = 0; }
            1 -> { row = 0; index = 1; col = 0; }
            2 -> { row = 0; index = 0; col = 1; }
            3 -> { row = 0; index = 1; col = 1; }
            4 -> { row = 0; index = 0; col = 2; }
            5 -> { row = 0; index = 1; col = 2; }
            6 -> { row = 0; index = 0; col = 3; }
            7 -> { row = 0; index = 1; col = 3; }
            8 -> { row = 0; index = 0; col = 4; }
            9 -> { row = 0; index = 1; col = 4; }
            10 -> { row = 0; index = 0; col = 5; }
            11 -> { row = 0; index = 1; col = 5; }
            12 -> { row = 0; index = 2; col = 5; }
            13 -> { row = 0; index = 3; col = 5; }

            14 -> { row = 1; index = 2; col = 5; }
            15 -> { row = 1; index = 3; col = 5; }
            16 -> { row = 2; index = 2; col = 5; }
            17 -> { row = 2; index = 3; col = 5; }
            18 -> { row = 3; index = 2; col = 5; }
            19 -> { row = 3; index = 3; col = 5; }
            20 -> { row = 4; index = 2; col = 5; }
            21 -> { row = 4; index = 3; col = 5; }
            22 -> { row = 5; index = 2; col = 5; }
            23 -> { row = 5; index = 3; col = 5; }
            24 -> { row = 5; index = 4; col = 5; }
            25 -> { row = 5; index = 5; col = 5; }

            26 -> { row = 5; index = 4; col = 4; }
            27 -> { row = 5; index = 5; col = 4; }
            28 -> { row = 5; index = 4; col = 3; }
            29 -> { row = 5; index = 5; col = 3; }
            30 -> { row = 5; index = 4; col = 2; }
            31 -> { row = 5; index = 5; col = 2; }
            32 -> { row = 5; index = 4; col = 1; }
            33 -> { row = 5; index = 5; col = 1; }
            34 -> { row = 5; index = 4; col = 0; }
            35 -> { row = 5; index = 5; col = 0; }
            36 -> { row = 5; index = 6; col = 0; }
            37 -> { row = 5; index = 7; col = 0; }

            38 -> { row = 4; index = 6; col = 0; }
            39 -> { row = 4; index = 7; col = 0; }
            40 -> { row = 3; index = 6; col = 0; }
            41 -> { row = 3; index = 7; col = 0; }
            42 -> { row = 2; index = 6; col = 0; }
            43 -> { row = 2; index = 7; col = 0; }
            44 -> { row = 1; index = 6; col = 0; }
            45 -> { row = 1; index = 7; col = 0; }
            46 -> { row = 0; index = 6; col = 0; }
            47 -> { row = 0; index = 7; col = 0; }
            else -> throw IllegalStateException()
        }
        println("$col $row $index")

        while (true) {

            (0 until opponentCount).map { scanner.nextLine() }
            val handSize = scanner.nextLine().toInt()
            val cards = (0 until handSize).map { scanner.nextLine().split(" ") }

            println("${cards[0][0]} 1")
        }
    }
}