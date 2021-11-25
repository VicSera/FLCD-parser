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
                println("${it.first} := ${it.second.map { production -> 
                        production.reduce{ acc, str -> "$acc $str" } 
                    }.reduce{ acc, str -> "$acc | $str" }
                }")
            }
            4 -> {
                print("Please input a non-terminal: ")
                val nonTerminal = readLine()
                val productions = grammar.productions.find { it.first == nonTerminal }

                productions?.second
                    ?.map { production -> production.reduce{ acc, str -> "$acc $str" } }
                    ?.reduce{ acc, str -> "$acc | $str" }
                    ?.let { println("$nonTerminal := $it") }
                    ?: println("Non-terminal $nonTerminal not found")

            }
            5 -> println(if (grammar.isCFG) "The grammar IS a CFG" else "The grammar is NOT a CFG")
        }
    }
}
