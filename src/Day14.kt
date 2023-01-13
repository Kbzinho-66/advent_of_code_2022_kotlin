import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTimedValue

private typealias Structure = Array<Array<ThingsInACave>>

private data class CavePosition(var x: Int, var y: Int)

private enum class ThingsInACave { Air, Rock, Sand, Source }

private class Cave(positionOfRocks: List<List<CavePosition>>) {
    val cave: Structure
    val source: CavePosition
    val leftmostRockAbsolute: Int
    val rightmostRockAbsolute: Int
    val deepestLevel: Int
    val leftmostRockRelative: Int
    val rightmostRockRelative: Int
    val height: Int
    val width: Int
    var sandGrains = 0
    var lastPositions = LinkedList<CavePosition>()

    init {
        var leftmostRock = Int.MAX_VALUE
        var rightmostRock = Int.MIN_VALUE
        var deepestRock = Int.MIN_VALUE
        for (path in positionOfRocks) {
            for ((x, y) in path) {
                if (x < leftmostRock) leftmostRock = x
                if (x > rightmostRock) rightmostRock = x
                if (y > deepestRock) deepestRock = y
            }
        }

        leftmostRockAbsolute = leftmostRock
        rightmostRockAbsolute = rightmostRock
        deepestLevel = deepestRock

        val emptySpace = 175
        leftmostRockRelative = emptySpace
        rightmostRockRelative = (rightmostRockAbsolute - leftmostRockAbsolute) + emptySpace
        width = emptySpace + (rightmostRockRelative - leftmostRockRelative) + emptySpace + 1
        height = deepestLevel + 3

        val paths = positionOfRocks.map { path ->
            path.map { (x, y) ->
                val newX = x - leftmostRockAbsolute + emptySpace
                CavePosition(newX, y)
            }
        }

        cave = Array(height) { Array(width) { ThingsInACave.Air } }

        for (path in paths) {
            var starting: CavePosition? = null
            for (position in path) {
                if (starting == null) {
                    starting = position
                } else {
                    val startingX = min(starting.x, position.x)
                    val startingY = min(starting.y, position.y)
                    val endingX = max(starting.x, position.x)
                    val endingY = max(starting.y, position.y)

                    for (y in startingY .. endingY) {
                        for (x in startingX .. endingX) {
                            cave[y][x] = ThingsInACave.Rock
                        }
                    }
                    starting = position
                }
            }
        }

        for (x in 0 until width) {
            cave[height-1][x] = ThingsInACave.Rock
        }

        source = CavePosition(500 - leftmostRockAbsolute + emptySpace, 0)
        cave[source] = ThingsInACave.Source
    }

    private fun print() {
        for (row in cave) {
            for (col in row) {
                when (col) {
                    ThingsInACave.Air -> print(" ")
                    ThingsInACave.Rock -> print("\u001B[30m#")
                    ThingsInACave.Sand -> print("\u001B[33m.")
                    ThingsInACave.Source -> print("\u001B[37m+")
                }
            }
            println()
        }
        println("\u001B[0m" + "=".repeat(width))
    }

    private fun nextPosition(current: CavePosition): CavePosition {
        lastPositions.addFirst(current)
        arrayOf(
            CavePosition(current.x, current.y + 1),
            CavePosition(current.x - 1, current.y + 1),
            CavePosition(current.x + 1, current.y + 1)
        ).forEach { position ->
//            if (isAbyss(position)) return position
            if (cave[position] == ThingsInACave.Air) return nextPosition(position)
        }

        lastPositions.removeFirst()
        return current
    }

    fun part1(): Int {
        while (true) {
            val settled = nextPosition(if (lastPositions.isEmpty()) source else lastPositions.removeFirst())
            if (isAbyss(settled)) {
                break
            } else {
                cave[settled] = ThingsInACave.Sand
                sandGrains++
            }
        }

        print()
        return sandGrains
    }

    fun part2(): Int {
        while (true) {
            val settled = nextPosition(source)
            if (settled == source) {
                cave[settled] = ThingsInACave.Sand
                sandGrains++
                break
            } else {
                cave[settled] = ThingsInACave.Sand
                sandGrains++
            }
        }

        print()
        return sandGrains
    }

    private operator fun Structure.get(position: CavePosition): ThingsInACave {
        return this[position.y][position.x]
    }

    private operator fun Structure.set(source: CavePosition, value: ThingsInACave) {
        this[source.y][source.x] = value
    }

    private fun isAbyss(position: CavePosition): Boolean {
        return position.x !in leftmostRockRelative..<rightmostRockRelative
    }
}
fun main() {
    fun parseInput(input: List<String>): List<List<CavePosition>> =
        input.map { line ->
            line.split("->")
                .map { position ->
                    val (x, y) = position.trim().split(',')
                    CavePosition(x.toInt(), y.toInt())
                }
        }

    // Testar os casos básicos
    val testRocks = parseInput(readInput("../inputs/Day14_test"))
    val testCave = Cave(testRocks)

    val rocks = parseInput(readInput("../inputs/Day14"))
    val cave = Cave(rocks)

    sanityCheck(testCave.part1(), 24)
    val (answer1, duration1) = measureTimedValue { cave.part1() }
    println("Parte 1 = $answer1 em $duration1")

    sanityCheck(testCave.part2(), 93)
    val (answer2, duration2) = measureTimedValue { cave.part2() }
    println("Parte 2 = $answer2 em $duration2")
}