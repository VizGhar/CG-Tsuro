package com.codingame.game

const val tileSize = 150

typealias Tile = Pair<Int, IntArray>
typealias Position = Pair<Int, Int>
val Tile.id : Int get() = first
val Tile.connections : IntArray get() = second
val Position.x : Int get() = first
val Position.y : Int get() = second

fun Tile.rotated(rotation: Int) = copy(second = when(rotation) {
    0 -> connections
    1 -> (connections.map { (it + 2) % 8 }).toIntArray()
    2 -> (connections.map { (it + 4) % 8 }).toIntArray()
    3 -> (connections.map { (it + 6) % 8 }).toIntArray()
    else -> throw IllegalArgumentException()
})

fun Tile.relativePositions(rotation: Int) : List<Position> =
    rotated(rotation).connections.map(::indexToRelativePosition)

fun indexToRelativePosition(index: Int) =
        when(index) {
            0 -> 50 to 0
            1 -> 100 to 0
            2 -> 150 to 50
            3 -> 150 to 100
            4 -> 100 to 150
            5 -> 50 to 150
            6 -> 0 to 100
            7 -> 0 to 50
            else -> throw IllegalStateException()
        }

fun indexDistance(from: Int, to: Int): Double =
        when {
            // by 1 even
            from % 2 == 0 && to == from + 1 -> 50.0
            to % 2 == 0 && from == to + 1 -> 50.0

            // by 1 odd
            from % 2 == 1 && (from + 1) % 8 == to -> 70.7
            to % 2 == 1 && (to + 1) % 8 == from -> 70.7

            // by 2
            (from + 2) % 8 == to -> 111.8
            (to + 2) % 8 == from -> 111.8

            // by 3 even
            from % 2 == 0 && (from + 3) % 8 == to -> 141.4
            to % 2 == 0 && (to + 3) % 8 == from -> 141.4

            // by 3 odd
            from % 2 == 1 && (from + 3) % 8 == to -> 150.0
            to % 2 == 1 && (to + 3) % 8 == from -> 150.0

            // by 4
            (from + 4) % 8 == to -> 158.1
            (to + 4) % 8 == from -> 158.1

            else -> throw IllegalStateException("from = $from ; to = $to")
        }

val tiles : List<Tile> = listOf(
        0 to intArrayOf(0, 1, 2, 3, 4, 5, 6, 7),
        1 to intArrayOf(0, 7, 1, 6, 2, 3, 4, 5),
        2 to intArrayOf(0, 7, 1, 2, 3, 6, 4, 5),
        3 to intArrayOf(0, 7, 1, 2, 3, 4, 5, 6),
        4 to intArrayOf(0, 6, 1, 7, 2, 3, 4, 5),

        5 to intArrayOf(0, 1, 2, 7, 3, 6, 4, 5),
        6 to intArrayOf(0, 1, 2, 6, 3, 7, 4, 5),
        7 to intArrayOf(0, 6, 1, 2, 3, 7, 4, 5),
        8 to intArrayOf(0, 1, 2, 6, 3, 4, 5, 7),
        9 to intArrayOf(0, 6, 1, 2, 3, 4, 5, 7),

        10 to intArrayOf(0, 2, 1, 6, 3, 4, 5, 7),
        11 to intArrayOf(0, 2, 1, 6, 3, 7, 4, 5),
        12 to intArrayOf(0, 2, 1, 7, 3, 6, 4, 5),
        13 to intArrayOf(0, 3, 1, 7, 2, 6, 4, 5),
        14 to intArrayOf(0, 3, 1, 6, 2, 7, 4, 5),

        15 to intArrayOf(0, 3, 1, 2, 4, 7, 5, 6),
        16 to intArrayOf(0, 3, 1, 2, 4, 6, 5, 7),
        17 to intArrayOf(0, 4, 1, 2, 3, 6, 5, 7),
        18 to intArrayOf(0, 4, 1, 2, 3, 7, 5, 6),
        19 to intArrayOf(0, 5, 1, 7, 2, 3, 4, 6),

        20 to intArrayOf(0, 5, 1, 6, 2, 7, 3, 4),
        21 to intArrayOf(0, 5, 1, 2, 3, 7, 4, 6),
        22 to intArrayOf(0, 5, 1, 3, 2, 7, 4, 6),
        23 to intArrayOf(0, 5, 1, 3, 2, 6, 4, 7),
        24 to intArrayOf(0, 4, 1, 3, 2, 6, 5, 7),

        25 to intArrayOf(0, 2, 1, 3, 4, 6, 5, 7),
        26 to intArrayOf(0, 5, 1, 4, 2, 7, 3, 6),
        27 to intArrayOf(0, 5, 1, 4, 2, 6, 3, 7),
        28 to intArrayOf(0, 4, 1, 5, 2, 6, 3, 7),
        29 to intArrayOf(0, 3, 1, 5, 2, 6, 4, 7),

        30 to intArrayOf(0, 3, 1, 5, 2, 7, 4, 6),
        31 to intArrayOf(0, 2, 1, 5, 3, 7, 4, 6),
        32 to intArrayOf(0, 3, 1, 6, 2, 4, 5, 7),
        33 to intArrayOf(0, 6, 1, 3, 2, 4, 5, 7),
        34 to intArrayOf(0, 3, 1, 6, 2, 5, 4, 7)
)
