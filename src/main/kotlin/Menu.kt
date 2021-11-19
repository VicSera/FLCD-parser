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
        """.trimIndent())

        val choice = readLine()!!.toInt()

        when(choice) {
            1 -> grammar.terminals.forEach { println(it) }
            2 -> grammar.nonTerminals.forEach { println(it) }
            3 -> grammar.productions.forEach { println("${it.first} -> ${it.second}") }
        }
    }
}
