fun main() {

    fun getSections(pair: String): Pair<Set<Int>, Set<Int>> {
        val (first, second) = pair.split(",").map { sections ->
            val (a, b) = sections.split("-")
            IntRange(a.toInt(), b.toInt()).toSet()
        }

        return Pair(first, second)
    }

    fun part1(input: List<String>): Int  =
        input.map { pair ->
            getSections(pair)
        }.count { (first, second) ->
            (first subtract second).isEmpty() || (second subtract first).isEmpty()
        }

    fun part2(input: List<String>): Int =
        input.map { pair ->
            getSections(pair)
        }.count{ (first, second) ->
            (first intersect second).isNotEmpty()
        }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day04_test")
    sanityCheck(part1(testInput), 2)
    sanityCheck(part2(testInput), 4)

    val input = readInput("../inputs/Day04")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}

