private fun String.indexOfMarker(size: Int) =
    this.windowed(size).indexOfFirst { seq -> seq.toSet().size == size }

fun main() {

    fun part1(datastream: String, sizeOfMarker: Int): Int =
        sizeOfMarker + datastream.indexOfMarker(sizeOfMarker)

    fun part2(input: String, sizeofMarker: Int): Int =
        sizeofMarker + input.indexOfMarker(sizeofMarker)

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day06_test").first()
    sanityCheck(part1(testInput, 4), 7)
    sanityCheck(part2(testInput, 14), 19)

    val input = readInput("../inputs/Day06").first()
    println("Parte 1 = ${part1(input, 4)}")
    println("Parte 2 = ${part2(input, 14)}")
}