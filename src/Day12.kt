import kotlin.properties.Delegates

private typealias Adjacencies = MutableList<Int>

private class Graph(input: List<String>) {
    private var rows by Delegates.notNull<Int>()
    private var cols by Delegates.notNull<Int>()

    private var start by Delegates.notNull<Int>()
    private var end by Delegates.notNull<Int>()

    private var heights: Array<IntArray>
    private var graph: Array<Adjacencies>
    private var inverted: Array<Adjacencies>

    init {
        rows = input.size
        cols = input.first().length
        heights = Array(rows) { IntArray(cols) }
        graph = Array(rows * cols) { mutableListOf() }
        inverted = Array(rows * cols) { mutableListOf() }
        input.forEachIndexed { row, s ->
            s.forEachIndexed { col, height ->
                heights[row][col] = when (height) {
                    'S' -> 'a'.also { start = row * cols + col }
                    'E' -> 'z'.also { end = row * cols + col }
                    else -> height
                } - 'a'
            }
        }
        buildGraph()
    }

    private fun buildGraph() {
        val deltaX = arrayOf(0, 1, 0, -1)
        val deltaY = arrayOf(-1, 0, 1, 0)

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val current = i * cols + j
                val currentHeight = heights[i][j]

                for (d in 0 until 4) {
                    val u = i + deltaY[d]
                    val v = j + deltaX[d]
                    val dest = u * cols + v

                    if (u in 0 until rows && v in 0 until cols) {
                        val destHeight = heights[u][v]

                        if (currentHeight >= destHeight || destHeight == currentHeight + 1) {
                            graph[current].add(dest)
                            inverted[dest].add(current)
                        }
                    }
                }
            }
        }
    }
    fun part1(): Int {
        // Aqui dá pra fazer uma BFS normal
        val distance = IntArray(graph.size) { -1 }
        distance[start] = 0

        val queue = ArrayDeque<Int>()
        queue.addLast(start)

        while (distance[end] == -1) {
            val curr = queue.removeFirst()
            val dist = distance[curr] + 1

            for (adj in graph[curr]) {
                if (distance[adj] == -1) {
                    distance[adj] = dist
                    queue.addLast(adj)
                }
            }
        }

        return distance[end]
    }

    fun part2(): Int {
        // Aqui, precisa de uma BFS modificada, partindo do destino, o lugar mais alto e parando
        // quando encontrar um lugar de elevação 0
        val distance = IntArray(graph.size) { -1 }
        distance[end] = 0

        val queue = ArrayDeque<Int>()
        queue.addLast(end)

        while (true) {
            val curr = queue.removeFirst()
            val dist = distance[curr]
            if (heights[curr/cols][curr%cols] == 0) return dist

            for (adj in inverted[curr]) {
                if (distance[adj] == -1) {
                    distance[adj] = dist + 1
                    queue.addLast(adj)
                }
            }
        }
    }
}

fun main() {
    // Testar os casos básicos
    val testGraph = Graph(readInput("../inputs/Day12_test"))
    sanityCheck(testGraph.part1(), 31)
    sanityCheck(testGraph.part2(), 29)

    val graph = Graph(readInput("../inputs/Day12"))
    println("Parte 1 = ${graph.part1()}")
    println("Parte 2 = ${graph.part2()}")
}