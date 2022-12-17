import kotlin.math.abs

operator fun String.component1() = when (this.substringBefore(' ')) {
    "U" -> Direction.UP
    "D" -> Direction.DOWN
    "L" -> Direction.LEFT
    else -> Direction.RIGHT
}
operator fun String.component2() = this.substringAfter(' ').toInt()

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class Position(val x: Int = 0, val y: Int = 0) {
    fun move(direction: Direction): Position =
        when (direction) {
            Direction.UP -> Position(this.x, this.y + 1)
            Direction.DOWN -> Position(this.x, this.y - 1)
            Direction.LEFT -> Position(this.x - 1, this.y)
            Direction.RIGHT -> Position(this.x + 1, this.y)
        }

    fun follow(other: Position): Position {
        if (this.distanceFrom(other) <= 1) return this

        var (x, y) = this
        if (this.x != other.x) x += if (this.x < other.x) 1 else -1
        if (this.y != other.y) y += if (this.y < other.y) 1 else -1

        return Position(x, y)
    }

    private fun distanceFrom(other: Position) =
        maxOf(abs(other.x - this.x), abs(other.y - this.y))

    override fun equals(other: Any?): Boolean =
        (other is Position) &&
        this.x == other.x && this.y == other.y

    override fun toString(): String = "(${this.x}, ${this.y})"
}

class Rope(size: Int) {
    private val knots = Array(size) { Position() }

    fun move(direction: Direction): Position {
        // Move head in given direction
        knots[0] = knots[0].move(direction)
        // And every knot follows the one in front of it
        for (i in 1 until knots.size) {
            val newPos = knots[i].follow(knots[i-1])
            if (knots[i] != newPos) {
                knots[i] = newPos
            } else {
                // If a knot stayed in place, so will every knot following it
                break
            }
        }

        return knots.last()
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val rope = Rope(2)
        val uniquePoints = mutableSetOf<Position>()

        for ((direction, times) in input) {
            repeat(times) {
                val tail = rope.move(direction)
                uniquePoints.add(tail)
            }
        }

        return uniquePoints.size
    }

    fun part2(input: List<String>): Int {
        val rope = Rope(10)
        val uniquePoints = mutableSetOf<Position>()

        for ((direction, times) in input) {
            repeat(times) {
                val tail = rope.move(direction)
                uniquePoints.add(tail)
            }
        }

        return uniquePoints.size
    }

    // Testar os casos básicos
    val testInput1 = readInput("../inputs/Day09_test")
    sanityCheck(part1(testInput1), 13)
    sanityCheck(part2(testInput1), 1)

    val testInput2 = readInput("../inputs/Day09_test2")
    sanityCheck(part2(testInput2), 36)

    val input = readInput("../inputs/Day09")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}