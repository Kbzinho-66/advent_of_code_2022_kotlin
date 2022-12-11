typealias Forest = List<List<Int>>

fun List<String>.toForest() = this.map { row -> row.map { it.digitToInt() } }

fun isVisible(forest: Forest, row: Int, col: Int): Boolean {
    // Árvores nas bordas são visíveis
    if (row == 0 || row == forest.size - 1 || col == 0 || col == forest[row].size - 1) return true

    val tree = forest[row][col]

    var visibleFromLeft   = true
    var visibleFromRight  = true
    var visibleFromTop    = true
    var visibleFromBottom = true

    // Primeiro verificar se tem alguma árvore maior na linha
    var i = 0
    while(i < forest[row].size) {
        when {
            visibleFromLeft  && i < col -> if (forest[row][i] >= tree)  visibleFromLeft = false
            visibleFromRight && i > col -> if (forest[row][i] >= tree)  visibleFromRight = false
        }
        i++
    }

    // Depois verificar a coluna
    i = 0
    while (i < forest.size) {
        when {
            visibleFromTop    && i < row -> if (forest[i][col] >= tree) visibleFromTop = false
            visibleFromBottom && i > row -> if (forest[i][col] >= tree) visibleFromBottom = false
        }
        i++
    }

    return (visibleFromTop || visibleFromRight || visibleFromBottom || visibleFromLeft)
}

fun scenicScore(forest: Forest, row: Int, col: Int): Int {
    if (row == 0 || row == forest.size - 1 || col == 0 || col == forest[row].size - 1) return 0

    val tree = forest[row][col]


    var viewingDistanceUp    = 0
    var viewingDistanceDown = 0
    var viewingDistanceLeft   = 0
    var viewingDistanceRight  = 0

    for (i in row - 1 downTo 0) {
        viewingDistanceUp++
        if (forest[i][col] >= tree) break
    }

    for (i in row + 1 until forest.size) {
        viewingDistanceDown++
        if (forest[i][col] >= tree) break
    }

    for (i in col - 1 downTo 0) {
        viewingDistanceLeft++
        if (forest[row][i] >= tree) break
    }

    for (i in col + 1 until forest[row].size) {
        viewingDistanceRight++
        if (forest[row][i] >= tree) break
    }

//    println("($row, $col) = $viewingDistanceUp * $viewingDistanceLeft * $viewingDistanceDown * $viewingDistanceRight")
    return viewingDistanceUp * viewingDistanceLeft * viewingDistanceDown * viewingDistanceRight

}
fun main() {

    fun part1(forest: Forest): Int {
        var total = 0

        for (row in forest.indices) {
            for (col in forest[row].indices) {
                if ( isVisible(forest, row, col) ) {
                    total += 1
                }
            }
        }

        return total
    }

    fun part2(forest: Forest): Int {
        var maxArea = 0

        for (row in forest.indices) {
            for (col in forest[row].indices) {
                val score = scenicScore(forest, row, col)
                if (score > maxArea) {
                    maxArea = score
                }
            }
        }

        return maxArea
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day08_test").toForest()
    sanityCheck(part1(testInput), 21)
    sanityCheck(part2(testInput), 8)

    val input = readInput("../inputs/Day08").toForest()
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}