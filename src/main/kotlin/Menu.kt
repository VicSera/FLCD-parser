class Menu(val grammar: Grammar) {
    fun display() {
        while (true) {
            menuLoop()
        }
    }

    private fun menuLoop() {
        println("""
            1. Display terminals
            2. Display non-terminals
            3. Display productions
            4. Display productions for a non-terminal
            5. CFG check
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
        }
    }
}
