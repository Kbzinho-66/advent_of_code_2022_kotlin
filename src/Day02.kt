private const val WIN = 6
private const val DRAW = 3
private const val LOSS = 0

private const val ROCK = 1
private const val PAPER = 2
private const val SCISSORS = 3

fun main() {
    operator fun String.component1() = this[0] - 'A'
    operator fun String.component2() = this[1]
    operator fun String.component3() = this[2] - 'X'

    fun part1(games: List<String>): Int {

        // Linhas representam o que o oponente jogou; colunas, o que eu joguei
        val points = arrayOf(
            intArrayOf(ROCK + DRAW, PAPER + WIN, SCISSORS + LOSS),
            intArrayOf(ROCK + LOSS, PAPER + DRAW, SCISSORS + WIN),
            intArrayOf(ROCK + WIN, PAPER + LOSS, SCISSORS + DRAW),
        )

        var total = 0
        games.forEach { round ->
            val (opponent, _, mine) = round
            total += points[opponent][mine]
        }

        return total
    }

    fun part2(games: List<String>): Int {

        // Linhas continuam representando o que o oponente jogou, mas as colunas passam a ser o resultado do jogo
        val points = arrayOf(
            intArrayOf(LOSS + SCISSORS, DRAW + ROCK    , WIN + PAPER),
            intArrayOf(LOSS + ROCK    , DRAW + PAPER   , WIN + SCISSORS),
            intArrayOf(LOSS + PAPER   , DRAW + SCISSORS, WIN + ROCK),
        )

        var total = 0
        games.forEach { round ->
            val (opponent, _ , outcome) = round
            total += points[opponent][outcome]
        }

        return total
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day02_test")
    sanityCheck(part1(testInput), 15)
    sanityCheck(part2(testInput), 12)

    val input = readInput("../inputs/Day02")
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}

