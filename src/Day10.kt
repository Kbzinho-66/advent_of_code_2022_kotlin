
private data class CycleState(val tick: Int, val register: Int ) {
    val signalStrength = tick * register
    val pixelIsVisible = (tick - 1) % 40 in register - 1 .. register + 1
}

fun main() {
    fun getAllStates(input: List<String>): List<CycleState> {
        val states: MutableList<CycleState> = mutableListOf()

        var x = 1
        var tick = 1

        input.forEach { instruction ->
            val op = instruction.substringBefore(' ')

            if (op == "noop") {
                states.add(CycleState(tick, x))
                tick++
            } else {
                val value = instruction.substringAfter(' ').toInt()
                repeat(2) {
                    states.add(CycleState(tick, x))
                    tick++
                }
                x += value
            }
        }

        return states
    }

    fun part1(states: List<CycleState>): Int {
        val sample = arrayOf(20, 60, 100, 140, 180, 220)
        return states
            .filter { s -> s.tick in sample }
            .sumOf { s -> s.signalStrength }
    }

    fun part2(states: List<CycleState>) {
        val pixels = states.map { s -> if (s.pixelIsVisible) '#' else '.' }
        pixels.chunked(40).forEach { println(it.joinToString(" ")) }
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day10_test")
    val testStates = getAllStates(testInput)
    sanityCheck(part1(testStates), 13140)
//    part2(testStates)

    val input = readInput("../inputs/Day10")
    val states = getAllStates(input)
    println("Parte 1 = ${part1(states)}")
    println("Parte 2")
    part2(states)
}