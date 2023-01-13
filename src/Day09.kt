import kotlin.math.abs
private operator fun String.component1() = when (this.substringBefore(' ')) {
    "U" -> Direction.UP
    "D" -> Direction.DOWN
    "L" -> Direction.LEFT
    else -> Direction.RIGHT
}
private operator fun String.component2() = this.substringAfter(' ').toInt()

private enum class Direction { UP, DOWN, LEFT, RIGHT }

private data class RopePosition(val x: Int = 0, val y: Int = 0) {
    fun move(direction: Direction): RopePosition =
        when (direction) {
            Direction.UP -> RopePosition(this.x, this.y + 1)
            Direction.DOWN -> RopePosition(this.x, this.y - 1)
            Direction.LEFT -> RopePosition(this.x - 1, this.y)
            Direction.RIGHT -> RopePosition(this.x + 1, this.y)
        }

    fun follow(other: RopePosition): RopePosition {
        if (this.distanceFrom(other) <= 1) return this

        var (x, y) = this
        if (this.x != other.x) x += if (this.x < other.x) 1 else -1
        if (this.y != other.y) y += if (this.y < other.y) 1 else -1

        return RopePosition(x, y)
    }

    private fun distanceFrom(other: RopePosition) =
        maxOf(abs(other.x - this.x), abs(other.y - this.y))

    override fun equals(other: Any?): Boolean =
        (other is RopePosition) &&
        this.x == other.x && this.y == other.y

    override fun toString(): String = "(${this.x}, ${this.y})"
}

private class Rope(size: Int) {
    private val knots = Array(size) { RopePosition() }

    fun move(direction: Direction): RopePosition {
        knots[0] = knots[0].move(direction)
        // Depois de mover a cabeça, todos os outros nós devem seguir o movimento do nó à frente
        for ( (head, tail) in knots.indices.zipWithNext()) {
            val newPos = knots[tail].follow(knots[head])
            if (knots[tail] == newPos) break
            knots[tail] = newPos
        }

        return knots.last()
    }
}

fun main() {
    fun executeMoves(input: List<String>, sizeOfRope: Int): Int {
        val rope = Rope(sizeOfRope)
        val uniquePoints = mutableSetOf<RopePosition>()

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
    sanityCheck(executeMoves(testInput1, 2), 13)
    sanityCheck(executeMoves(testInput1, 10), 1)

    val testInput2 = readInput("../inputs/Day09_test2")
    sanityCheck(executeMoves(testInput2, 10), 36)

    val input = readInput("../inputs/Day09")
    println("Parte 1 = ${executeMoves(input, 2)}")
    println("Parte 2 = ${executeMoves(input, 10)}")
}