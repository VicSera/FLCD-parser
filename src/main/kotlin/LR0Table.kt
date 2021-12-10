import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment

class LR0Table(val rows: Array<LR0Row>, val grammar: Grammar) {
    fun getActionForRow(rowNumber: Int): String {
        return rows[rowNumber].action
    }

    fun getNextState(currentState: Int, nextSymbol: String): Int? {
        return rows[currentState].symbolToState[nextSymbol]
    }

    override fun toString(): String {
        val table = AsciiTable()
        val header = listOf("", "ACTION").toMutableList()
        val symbols = grammar.allSymbols
        header.addAll(symbols)

        table.addRule()
        table.addRow(header)

        rows.forEach { row ->
            table.addRule()
            val rowContent = listOf(row.stateNumber.toString(), row.action).toMutableList()
            symbols.forEach { symbol -> rowContent.add(row.symbolToState[symbol]?.toString() ?: "") }
            table.addRow(rowContent)
        }

        table.addRule()

        table.setTextAlignment(TextAlignment.CENTER)

        return table.render(1000)
    }
}

class LR0Row(val stateNumber: Int, val action: String, val symbolToState: Map<String, Int>)
