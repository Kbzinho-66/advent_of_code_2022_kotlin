fun main() {
    operator fun String.component1() = this.subSequence(0, this.length / 2).toSet()
    operator fun String.component2() = this.subSequence(this.length / 2, this.length).toSet()

    fun priority(char: Char): Int {
        return when (char) {
            in 'a'..'z' -> char - 'a' + 1
            in 'A'..'Z' -> 26 + (char - 'A' + 1)
            else -> 0
        }
    }

    fun part1(input: List<String>): Int {
        var total = 0
        for (items in input) {
            val (comp1, comp2) = items
            for (char in comp1) {
                if (char in comp2) total += priority(char)
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var total = 0

        val iter: ListIterator<String> = input.listIterator()
        while (iter.hasNext()) {
            val set1 = iter.next().toSet()
            val set2 = iter.next().toSet()
            val set3 = iter.next().toSet()

            for (item in set1) {
                if (item in set2 && item in set3) total += priority(item)
            }
        }

        return total
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day03_test")
    sanityCheck(part1(testInput), 157)
    sanityCheck(part2(testInput), 70)

    val input = readInput("../inputs/Day03")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}

