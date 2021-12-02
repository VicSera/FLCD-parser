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
        val allSymbols = grammar.terminals.toMutableList()
        allSymbols.addAll(grammar.nonTerminals)

        while (modified) {
            modified = false
            val tmpStates = emptyList<State>().toMutableList()
            states.forEach { state ->
                allSymbols.forEach { symbol ->
                    val resultingClosure = goTo(state, symbol)
                    // TODO: second condition is not properly checked -> preexisting states get added every time resulting in an infinite loop
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
}
