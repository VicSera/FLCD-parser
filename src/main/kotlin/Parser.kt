import de.vandermeer.asciitable.AsciiTable
import java.util.*

class Parser(val grammar: Grammar) {
    fun closure(productions: List<AugmentedProduction>): List<AugmentedProduction> {
        val result = productions.toMutableList()
        var modified = true

        while (modified) {
            val tmpResult = emptyList<AugmentedProduction>().toMutableList()
            modified = false
            result.forEach { inputProduction ->
                val nonTerminal = inputProduction.getNextSymbol()
                if (nonTerminal != null) {
                    grammar.productionsForNonTerminal(nonTerminal).forEach {
                        val exists = result.find {
                            existing -> existing.production.second == it && existing.dotPosition == 0
                        } != null
                        if (!exists) {
                            tmpResult.add(AugmentedProduction(Pair(nonTerminal, it)))
                            modified = true
                        }
                    }
                }
            }
            result.addAll(tmpResult)
        }

        return result
    }

    fun goTo(state: State, symbol: String): List<AugmentedProduction> {
        return closure(state.productions
            .filter { it.getNextSymbol() == symbol }
            .map { it.moveToNextPosition() })
    }

    fun canonicalCollection(): List<State> {
        var nextState = 0
        val state0 = State(nextState++, closure(grammar.augmentedProductionsForNonTerminal("(start)")))
        val states = listOf(state0).toMutableList()
        var modified = true
        val allSymbols = grammar.allSymbols

        while (modified) {
            modified = false
            val tmpStates = emptyList<State>().toMutableList()
            states.forEach { state ->
                allSymbols.forEach { symbol ->
                    val resultingClosure = goTo(state, symbol)
                    if (resultingClosure.isNotEmpty() && states.find { it.productions.checkEqual(resultingClosure) } == null) {
                        tmpStates.add(State(nextState++, resultingClosure))
                        modified = true
                    }
                }
            }
            states.addAll(tmpStates)
        }

        return states
    }

    fun createTable(): LR0Table {
        val canonicalCollection = canonicalCollection()
        val allSymbols = grammar.allSymbols
        val rows: Array<LR0Row> = Array(canonicalCollection.size) { stateNumber ->
            val state = canonicalCollection[stateNumber]
            val symbolToState = emptyMap<String, Int>().toMutableMap()
            allSymbols.forEach { symbol ->
                val resultingClosure = goTo(state, symbol)
                canonicalCollection.find { it.productions.checkEqual(resultingClosure) }
                    ?.let { symbolToState[symbol] = it.number }
            }

            var action = ""
            if (symbolToState.isNotEmpty())
                action = "shift"
            else if (state.productions.size == 1 && state.productions.first().production.first == "(start)")
                action = "accept"
            else if (state.productions.size == 1) {
                val augmentedProduction = state.productions.first()
                val productionNumber = grammar.productions.indexOfFirst { it.equalsProduction(augmentedProduction.production) }
                action = "reduce$productionNumber"
            }
            LR0Row(stateNumber, action, symbolToState)
        }

        return LR0Table(rows, grammar)
    }

    fun parse(sequence: List<String>) {
        val table = createTable()
//        println(table.toString())
        val asciiTable = AsciiTable()

        asciiTable.addRule()
        asciiTable.addRow("Work stack", "Input stack", "Output band")
        asciiTable.addRule()

        val workStack = Stack<Pair<String, Int>>()
        workStack.push(Pair("$", 0))
        val inputStack = Stack<String>()
        inputStack.push("$")
        sequence.reversed().forEach { inputStack.push(it) }
        val outputBand = mutableListOf<Int>()

        while (true) {
            val workStackStr = workStack.map { "${it.first}${it.second}" }.reduce{ acc, s -> "$acc$s" }
            val inputStackStr = inputStack.reduce { acc, s -> "$acc$s" }.reversed()
            val outputBandStr =
                if (outputBand.isNotEmpty())
                    outputBand.map{ it.toString() }.reduce { acc, s -> "$acc$s" }
                else "\u03b5"

            asciiTable.addRow(workStackStr, inputStackStr, outputBandStr)

            val currentWorkState = workStack.peek()
            val currentState = currentWorkState.second

            when(val action = table.getActionForRow(currentState)) {
                "accept" -> {
                    asciiTable.addRow("\$acc", inputStackStr, outputBandStr)
                    break
                }
                "shift" -> {
                    val nextSymbol = inputStack.pop()
                    val nextState = table.getNextState(currentState, nextSymbol)
                        ?: throw Exception("Tried to go to a weird state ($currentState, $nextSymbol -> ?)")
                    workStack.push(Pair(nextSymbol, nextState))
                }
                else -> {
                    val productionNumber = extractProductionNumber(action) ?: throw Exception("Invalid reduce action: $action")
                    val production = grammar.productions[productionNumber]
                    val poppedSymbols = emptyList<String>().toMutableList()

                    while (poppedSymbols != production.second)
                        poppedSymbols.add(0, workStack.pop().first)

                    val nextState = table.getNextState(workStack.peek().second, production.first) ?: throw Exception("Tried to go to a weird state")
                    workStack.push(Pair(production.first, nextState))

                    outputBand.add(0, productionNumber)
                }
            }
        }

        asciiTable.addRule()
        println(asciiTable.render())
    }

    fun extractProductionNumber(reduceAction: String): Int? {
        return "reduce([0-9]+)".toRegex().matchEntire(reduceAction)?.groupValues?.get(1)?.toInt()
    }
}
