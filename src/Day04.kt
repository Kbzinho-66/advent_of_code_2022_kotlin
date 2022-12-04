fun main() {

    infix fun IntRange.fullyOverlaps(other: IntRange): Boolean =
        first < other.first && last >= other.last

    infix fun IntRange.overlaps(other: IntRange): Boolean =
        first <= other.last && last >= other.first

    fun getSections(pair: String): Pair<IntRange, IntRange> {
        val (first, second) = pair.split(",").map { sections ->
            val (a, b) = sections.split("-")
            IntRange(a.toInt(), b.toInt())
        }

        return Pair(first, second)
    }

    fun part1(input: List<String>): Int  =
        input.map { pair ->
            getSections(pair)
        }.count { (elf1, elf2) ->
            ( elf1 fullyOverlaps elf2 || elf2 fullyOverlaps elf1 )
        }

    fun part2(input: List<String>): Int =
        input.map { pair ->
            getSections(pair)
        }.count{ (elf1, elf2) ->
            elf1 overlaps elf2
        }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day04_test")
    sanityCheck(part1(testInput), 2)
    sanityCheck(part2(testInput), 4)

    val input = readInput("../inputs/Day04")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}

