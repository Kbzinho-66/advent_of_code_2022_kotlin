fun main() {
    fun part1(calories: List<Int>): Int {
        return calories[0]
    }

    fun part2(calories: List<Int>): Int {
        return calories.slice(0..2).sum()
    }

    fun getCalories(input: List<String>): List<Int> {
        var carried = 0
        val caloriesPerElf: MutableList<Int> = mutableListOf<Int>()

        val it: ListIterator<String> = input.listIterator()
        while (it.hasNext()) {
            val cal = it.next()
            if (cal.isNotEmpty()) carried += cal.toInt()

            // Esses ifs tão separados pra pegar o último número do arquivo
            if (cal.isEmpty() || !it.hasNext()) {
                caloriesPerElf.add(carried)
                carried = 0
            }
        }

        return caloriesPerElf.sortedDescending()
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day01_test")
    val testCalories = getCalories(testInput)
    sanityCheck(part1(testCalories), 24000)
    sanityCheck(part2(testCalories), 45000)

    val input = readInput("../inputs/Day01")
    val caloriesPerElf = getCalories(input)
    println("Parte 1 = ${part1(caloriesPerElf)}")
    println("Parte 2 = ${part2(caloriesPerElf)}")
}
