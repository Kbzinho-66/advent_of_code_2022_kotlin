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
        // Decompor a string nos seus dois compartimentos
        return input.map { (comp1, comp2) ->
            // Encontrar itens comuns aos dois
            comp1 intersect comp2
        }.flatten().sumOf { priority(it) } // E somar a prioridade de todos
    }

    fun part2(input: List<String>): Int {
        // Pegar grupos de 3 mochilas
        return input.chunked(3)
            .map { group ->
                // O zipWithNext vai comparar o primeiro com o segundo e o segundo com o terceiro
                group.zipWithNext()
                    // Separar nos dois compartimentos
                    .map { (first, second) ->
                        // Daqui vão sair os elementos comuns aos dois
                        first.toSet() intersect second.toSet()
                    }
            }.flatMap { shared ->
                // Como foi usado um grupo de 3, vai ter n-1 conjuntos de elementos comuns
                shared[0] intersect shared[1]
            }.sumOf { priority(it) }
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day03_test")
    sanityCheck(part1(testInput), 157)
    sanityCheck(part2(testInput), 70)

    val input = readInput("../inputs/Day03")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}

