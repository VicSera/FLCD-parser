import java.io.File
import java.lang.RuntimeException

class Grammar(file: File) {
    val terminals: Set<String>
    val nonTerminals: Set<String>
    val productions: Set<Pair<String, String>>

    init {
        val nonTerminalStr = "[a-zA-Z_]+"
        val terminal = "[a-zA-Z0-9_!#$%^&*()\\[\\]{}<>]+"
        val tmpNonTerminal = mutableSetOf<String>()
        val tmpTerminals = mutableSetOf<String>()
        val tmpProductions = mutableSetOf<Pair<String, String>>()

        file.forEachLine { line ->
            if (line.isNotEmpty() && line.isNotBlank()) {
                val nonTerminal = "^${nonTerminalStr}".toRegex().find(line)?.value
                    ?: throw RuntimeException("Line doesn't define any nonterminal")

                tmpNonTerminal.add(nonTerminal)

                val terminals = "\"${terminal}\"".toRegex().findAll(line).map { it.value }
                tmpTerminals.addAll(terminals)

                val productionsRaw = "^$nonTerminalStr := (.*)".toRegex().matchEntire(line)?.groupValues?.drop(1)?.get(0) ?: throw RuntimeException("Invalid nonterminal definition")
                val productionPairs = productionsRaw.split("|").map { Pair(nonTerminal, it.trim()) }
                tmpProductions.addAll(productionPairs)
            }

        }

        nonTerminals = tmpNonTerminal.toSet()
        terminals = tmpTerminals.toSet()
        productions = tmpProductions.toSet()
    }
}
