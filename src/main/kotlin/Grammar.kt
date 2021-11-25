import java.io.File
import java.lang.RuntimeException

class Grammar(file: File) {
    val isCFG: Boolean
    val terminals: Set<String>
    val nonTerminals: Set<String>
    val productions: List<Pair<String, List<List<String>>>>

    private val nonTerminalRegex = "[a-zA-Z_]+".toRegex()

    init {
        val leftHandSideStr = "[a-zA-Z_ \"]+"
        val terminal = "[a-zA-Z0-9_!#$%^&*()\\[\\]{}<>]+"

        val tmpNonTerminal = mutableSetOf<String>()
        val tmpTerminals = mutableSetOf<String>()
        val tmpProductions = mutableListOf<Pair<String, List<List<String>>>>()
        var tmpIsCFG = true

        file.forEachLine { line ->
            if (line.isNotEmpty() && line.isNotBlank()) {
                val terminals = "\"${terminal}\"".toRegex().findAll(line).map { it.value }
                tmpTerminals.addAll(terminals)

                val nonTerminalDefinition = "^($leftHandSideStr):=(.*)".toRegex().matchEntire(line)
                    ?.groupValues?.drop(1)

                val leftHandSide = nonTerminalDefinition?.getOrNull(0)?.trim()
                    ?: throw RuntimeException("Line doesn't define any non-terminal: $line")

                if (nonTerminalRegex.matches(leftHandSide))
                    tmpNonTerminal.add(leftHandSide)
                else {
                    tmpIsCFG = false
                    tmpNonTerminal.addAll(findNonTerminals(leftHandSide))
                }

                val productionsRaw = nonTerminalDefinition.getOrNull(1)
                    ?: throw RuntimeException("Invalid productions for left hand side $leftHandSide: $line")
                val pair = Pair(leftHandSide, productionsRaw.split("|").map { it.trim().split(" ") })
                tmpProductions.add(pair)
            }
        }

        nonTerminals = tmpNonTerminal.toSet()
        terminals = tmpTerminals.toSet()
        productions = tmpProductions.toList()
        isCFG = tmpIsCFG
    }

    private fun findNonTerminals(str: String): List<String> {
        return str.split(" ").filter { nonTerminalRegex.matches(it) }
    }
}
