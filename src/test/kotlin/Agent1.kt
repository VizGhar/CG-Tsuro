import java.util.*

typealias Tile = Pair<Int, IntArray>

object Agent1 {

    data class Player(
            val lastPlayedTile: Int,
            val lastPlayerTileConnections: IntArray,
            val col: Int,
            val row: Int,
            val index: Int,
            val startIndex: Int)

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
        val playerCount = input.nextInt()

        var firstMove = true
        // game loop
        while (true) {
            val players = (0 until playerCount).map {
                Player(
                        input.nextInt(),
                        intArrayOf(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(),
                                input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt()),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt(),
                        input.nextInt()
                )
            }

            System.err.println(players[0])

            for(player in players) {
                if (player.col >= 0 && player.row >= 0 && player.col < 6 && player.row < 6) {
                    board[player.col][player.row] = player.lastPlayedTile to player.lastPlayerTileConnections
                }
            }

            myPositionCol = players[0].col
            myPositionRow = players[0].row
            myPositionIndex = players[0].index

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
                if (newPosition != null) {
                    myPositionCol = newPosition.col
                    myPositionRow = newPosition.row
                    myPositionIndex = newPosition.index
                    println("PLACE ${tile.id} $rotation Yeah boy")
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
        val positions = (0..47).toMutableList()
        positions.removeAll { it in players.map { it.startIndex } }
        println("START ${positions.random()}")
    }
}