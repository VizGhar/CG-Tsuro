import java.util.*

typealias Tile = Pair<Int, IntArray>

object Agent1 {

    data class Player(val lastPlayedTile: Int, val lastPlayedTileRotation: Int, val col: Int, val row: Int, val index: Int)

    val Tile.id : Int get() = first
    val Tile.connections : IntArray get() = second

    fun Tile.rotated(rotation: Int) = copy(second = when(rotation) {
        0 -> connections
        1 -> (connections.map { (it + 2) % 8 }).toIntArray()
        2 -> (connections.map { (it + 4) % 8 }).toIntArray()
        3 -> (connections.map { (it + 6) % 8 }).toIntArray()
        else -> throw IllegalArgumentException()
    })

    @JvmStatic
    fun main(args : Array<String>?) {
        val input = Scanner(System.`in`)
        val opponentCount = input.nextInt()

        var firstMove = true
        // game loop
        while (true) {
            val players = (0 until opponentCount).map {
                Player(
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt()
                )
            }
            val tileCount = input.nextInt()
            val tiles = (0 until tileCount).map {
                input.nextInt() to intArrayOf(
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt()
                )
            }

            if (firstMove) {
                firstMove = false
                doFirstMove(players)
            } else {
                doMove(tiles)
            }
        }
    }

    val board = Array<Array<Tile?>>(6) { Array(6) { null } }

    var myPositionCol = -1
    var myPositionRow = -1
    var myPositionIndex = -1

    fun doMove(tiles: List<Tile>) {
        for (tile in tiles) {
            for (rotation in 0..3) {
                val tileToPlace = tile.rotated(rotation)

                board[myPositionCol][myPositionRow] = tileToPlace
                val newPosition = newPosition()
                if (newPosition!=null) {
                    myPositionCol = newPosition.col
                    myPositionRow = newPosition.row
                    myPositionIndex = newPosition.index
                    println("PLACE ${tile.id} $rotation")
                    return
                }
            }
        }
        println("PLACE ${tiles[0].id} 0")
    }

    data class Position(val col: Int, val row: Int, val index: Int)

    fun newPosition() : Position? {
        var actCol = myPositionCol
        var actRow = myPositionRow
        var actualIndex = myPositionIndex
        while (actCol >= 0 && actRow >= 0 && actCol < 6 && actRow < 6 && board[actCol][actRow] != null) {
            val actualPiece = board[actCol][actRow]
            val target = actualPiece!!.connections.toList()
                    .chunked(2)
                    .first { it.contains(actualIndex) }
                    .first { it != actualIndex }

            when (target) {
                0 -> { actRow--; actualIndex = 5;}
                1 -> { actRow--; actualIndex = 4;}
                2 -> { actCol++; actualIndex = 7;}
                3 -> { actCol++; actualIndex = 6;}
                4 -> { actRow++; actualIndex = 1;}
                5 -> { actRow++; actualIndex = 0;}
                6 -> { actCol--; actualIndex = 3;}
                7 -> { actCol--; actualIndex = 2;}
            }
        }
        return if (actCol >= 0 && actRow >= 0 && actCol < 6 && actRow < 6) {
            Position(actCol, actRow, actualIndex)
        } else {
            null
        }
    }

    fun doFirstMove(players: List<Player>) {
        // TODO: dont pick same position as player already have
        val position = (0..47).random()
        val row: Int
        val index: Int
        val col: Int

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

        myPositionCol = col
        myPositionRow = row
        myPositionIndex = index

        println("START $col $row $index")
    }
}