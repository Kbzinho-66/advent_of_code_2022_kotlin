private fun String.getValues() = this
    .split(" ")
    .filter { parts -> parts.all { it.isDigit() } }
    .map { it.toInt() }

private fun Array<ArrayDeque<Char>>.move(from: Int, to: Int) {
    this[to].add(this[from].removeLastOrNull() ?: ' ')
}

private fun Array<ArrayDeque<Char>>.move(n: Int, from: Int, to: Int) {
    this[from].takeLast(n).forEach { this[to].add(it) }.also {
        repeat (n) { this[from].removeLast() }
    }
}

private data class StacksState(
    val stacks: Array<ArrayDeque<Char>>,
    val moves: List<String>
)

private fun parseInput(input: List<String>): StacksState {
    val stacks = initializeStacks(input.takeWhile { it.isNotEmpty() })
    val moves  = input.filter { it.startsWith("move") }

    return StacksState(stacks, moves)
}

private fun initializeStacks(input: List<String>): Array<ArrayDeque<Char>> {
    val brackets = Regex("[\\[\\]]")

    val numberOfStacks = input.last().takeLast(4).trim().toInt()
    val stacks = Array<ArrayDeque<Char>>(numberOfStacks) { ArrayDeque() }

    input.dropLast(1).reversed().map { row ->
        val crates = row.chunked(4).map { rawCrate ->
            val crate = rawCrate.trim().replace(brackets, "")
            if (crate.isNotEmpty()) crate[0] else ' '
        }

        crates.forEachIndexed { s, crate ->
            if (crate != ' ') {
                stacks[s].add(crate)
            }
        }
    }

    return stacks
}

fun main() {
    fun part1(input: StacksState): String {
        val stacks = input.stacks

        for (move in input.moves) {
            val (n, from, to) = move.getValues()
            repeat (n) {
                stacks.move(from - 1, to - 1)
            }
        }

        return stacks.fold(""){ top, crate ->
            top + crate.last()
        }
    }

    fun part2(input: StacksState): String {
        val stacks = input.stacks

        for (move in input.moves) {
            val (n, from, to) = move.getValues()
            stacks.move(n, from - 1, to - 1)
        }

        return stacks.fold(""){ top, crate ->
            top + crate.last()
        }
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day05_test")
    sanityCheck(part1(parseInput(testInput)), "CMZ")
    sanityCheck(part2(parseInput(testInput)), "MCD")

    val input = readInput("../inputs/Day05")
    // Como as funções são impuras, precisa criar um "StacksState" para cada uma delas
    println("Parte 1 = ${part1(parseInput(input))}")
    println("Parte 2 = ${part2(parseInput(input))}")
}