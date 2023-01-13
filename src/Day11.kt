import java.lang.Exception

private data class WorryOperation(val operator: String, val value: String) {
    fun apply(starting: Long): Long {
        val actualValue = if (value == "old") starting else value.toLong()
        val result = when (operator) {
            "*" -> starting * actualValue
            "+" -> starting + actualValue
            else -> throw Exception("Invalid operator")
        }
        return result
    }
}
private data class Monkey(
    val items: ArrayDeque<Long>,
    val operation: WorryOperation,
    val divisor: Long,
    val recipientWhenTrue: Int,
    val recipientWhenFalse: Int,
    var inspections: Int = 0
) {
    fun next(worryLevel: Long): Int =
        if ( worryLevel % divisor == 0L) recipientWhenTrue else recipientWhenFalse
}

fun main() {
    fun studyMonkeys(input: List<String>): ArrayList<Monkey> {
        val monkeys = arrayListOf<Monkey>()
        val operatorPosition = "  Operation: new = old ".length

        var items: ArrayDeque<Long> = ArrayDeque()
        var operation: WorryOperation? = null
        var divisor: Long? = null
        var recipientWhenTrue: Int? = null
        var recipientWhenFalse: Int? = null

        for (line in input) {
            val trimmedLine = line.trim()
            when {
                trimmedLine.startsWith("Monkey") -> continue
                trimmedLine.startsWith("Starting items: ") -> {
                    line
                        .substringAfter(": ")
                        .split(", ")
                        .map { items.add(it.toLong()) }
                }
                trimmedLine.startsWith("Operation: ") -> {
                    val operator = line.substring(operatorPosition, operatorPosition + 1)
                    val value = line.substringAfter("$operator ")
                    operation = WorryOperation(operator, value)
                }
                trimmedLine.startsWith("Test: ") -> {
                    divisor = line.substringAfter("divisible by ").toLong()
                }
                trimmedLine.startsWith("If true: ") -> {
                    recipientWhenTrue = line.substringAfter("monkey ").toInt()
                }
                trimmedLine.startsWith("If false: ") -> {
                    recipientWhenFalse = line.substringAfter("monkey ").toInt()
                }
                else -> {
                    val newMonkey = Monkey(
                        items,
                        operation!!,
                        divisor!!,
                        recipientWhenTrue!!,
                        recipientWhenFalse!!
                    )
                    monkeys.add(newMonkey)

                    items = ArrayDeque()
                    operation = null
                    divisor = null
                    recipientWhenTrue = null
                    recipientWhenFalse = null
                }
            }
        }

        return monkeys
    }

    fun part1(monkeys: List<Monkey>): Int {
        for (round in 1..20) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    monkey.inspections++
                    val item = monkey.items.removeFirst()
                    val op = monkey.operation
                    val newWorryLevel = op.apply(item) / 3
                    val next = monkey.next(newWorryLevel)
                    monkeys[next].items.addLast(newWorryLevel)
                }
            }
        }

        val (top, second) = monkeys
            .map { monkey -> monkey.inspections }
            .sortedDescending()
            .take(2)

        return top * second
    }

    fun part2(monkeys: List<Monkey>): ULong {
        // The product of all divisors can drastically reduce the actual number while
        // keeping its remainder the same for each divisor.
        val modulus: Long = monkeys
            .map { monkey -> monkey.divisor }
            .fold(1) { total, curr -> total * curr }

        for (round in 1..10000) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    monkey.inspections++
                    val item = monkey.items.removeFirst()
                    val op = monkey.operation
                    val newWorryLevel = op.apply(item) % modulus
                    val next = monkey.next(newWorryLevel)
                    monkeys[next].items.addLast(newWorryLevel)
                }
            }
        }

        val (top, second) = monkeys
            .map { monkey -> monkey.inspections }
            .sortedDescending()
            .take(2)
            .map { it.toULong() }

        return top * second
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day11_test")
    var testMonkeys = studyMonkeys(testInput)
    sanityCheck(part1(testMonkeys), 10605)
    // Since Monkeys are stateful, it's safer to completely wipe them
    testMonkeys = studyMonkeys(testInput)
    sanityCheck(part2(testMonkeys), 2713310158UL)

    val input = readInput("../inputs/Day11")
    val monkeys = studyMonkeys(input)
    println("Parte 1 = ${part1(monkeys)}")
    println("Parte 2 = ${part2(monkeys)}")
}