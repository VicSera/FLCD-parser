class Menu(val grammar: Grammar) {
    private val parser = Parser(grammar)

    fun display() {
        while (true) {
            try {
                menuLoop()
            } catch(exception: ParserException) {
                println("Parser failed: ${exception.message}")
            }
        }
    }

    private fun menuLoop() {
        println("""
            1. Display terminals
            2. Display non-terminals
            3. Display productions
            4. Display productions for a non-terminal
            5. CFG check
            6. Print canonical collection
            7. Print goto table
            8. Parse sequence
            9. Parse PIF file
        """.trimIndent())

        when(readLine()!!.toInt()) {
            1 -> grammar.terminals.forEach { println(it) }
            2 -> grammar.nonTerminals.forEach { println(it) }
            3 -> grammar.productions.forEach {
                println("${it.first} := ${it.second.reduce { acc, s -> "$acc $s" }}")
            }
            4 -> {
                print("Please input a non-terminal: ")
                val nonTerminal = readLine()!!
                val productions = grammar.productionsForNonTerminal(nonTerminal)

                productions
                    .map { production -> production.reduce{ acc, str -> "$acc $str" } }
                    .reduce{ acc, str -> "$acc | $str" }
                    .let { println("$nonTerminal := $it") }

            }
            5 -> println(if (grammar.isCFG) "The grammar IS a CFG" else "The grammar is NOT a CFG")
            6 -> parser.canonicalCollection().forEach { println(it) }
            7 -> println(parser.createTable())
            8 -> {
                val sequence = listOf("{", "Print", "(", "constant", ")", "}")
                parser.parse(sequence.map{ it.toString() }).forEach { print(it) }
                println()
            }
            9 -> {
                val fileName = readLine()!!
                parser.parseFile(fileName).forEach { print(it) }
                println()
            }
        }
    }
}
