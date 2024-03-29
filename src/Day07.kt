private fun addFile(map: MutableMap<String, Int>, cwd: String, size: Int) {
    // Se ele não existir no mapa ainda, inicializa ele
    map.putIfAbsent(cwd, 0)
    // Somar o tamanho passado tanto pros que já existiam quando os que foram inicializados agora
    map[cwd] = map[cwd]!! + size

    // Se aqui o cwd estiver vazio, chegou na raiz e não tem mais o que subir
    if (cwd.isEmpty()) return

    // Se não, subir um nível do endereço e repetir o processo
    val parent = cwd.substringBeforeLast('/')
    addFile(map, parent, size)
}

fun main() {
    fun calculateSizes(input:List<String>): Map<String, Int> {
        val directories: MutableMap<String, Int> = hashMapOf(Pair("", 0))

        var cwd = ""
        for (line in input) {
            when {
                line.startsWith("$ ls") -> {}
                line.startsWith("$ cd") -> {
                    // Se for uma mudança de diretório, atualizar o endereço atual
                    if (line.endsWith("..")) {
                        cwd = cwd.substringBeforeLast("/", "")
                    } else {
                        cwd += ( if (line.isEmpty()) "" else "/" ) + line.substringAfterLast(' ')
                    }
                }
                else -> {
                    if (line.startsWith("dir")) {
                        // Se for um diretório, inicializar ele no map
                        val dir = "/" + line.substringAfter(' ')
                        addFile(directories, cwd + dir, 0)

                    } else {
                        // Se for um arquivo, adicionar o tamanho dele em todas as pastas que o contêm
                        val size = line.substringBefore(' ').toInt()
                        addFile(directories, cwd, size)
                    }
                }
            }
        }

        return directories
    }

    fun part1(input: List<String>): Int = calculateSizes(input).values.filter { it <= 100000 }.sum()

    fun part2(input: List<String>): Int {
        val totalDisk = 70000000
        val requiredDisk = 30000000
        val files = calculateSizes(input)
        val freeDisk = totalDisk - files[""]!!

        return files
            .filter { (key, size) -> key.isNotEmpty() && size + freeDisk >= requiredDisk }
            .minOf { (_, size) -> size }
    }

    // Testar os casos básicos
    val testInput = readInput("../inputs/Day07_test").drop(1)
    sanityCheck(part1(testInput), 95437)
    sanityCheck(part2(testInput), 24933642)

    // Tirar o primeiro comando que é sempre "$ cd /"
    val input = readInput("../inputs/Day07").drop(1)
    println("Parte 1 = ${part1(input)}")
    println("Parte 2 = ${part2(input)}")
}