class AugmentedProduction(
    val production: Pair<String, List<String>>,
    val dotPosition: Int = 0
) {

    fun getNextSymbol(): String? {
        if (dotPosition >= production.second.size)
            return null

        val next = production.second[dotPosition]

        return next
    }

    fun moveToNextPosition(): AugmentedProduction {
        return AugmentedProduction(production, dotPosition + 1)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is AugmentedProduction)
            production.equalsProduction(other.production)
        else false
    }

    override fun hashCode(): Int {
        var result = production.hashCode()
        result = 31 * result + dotPosition
        return result
    }

    override fun toString(): String {
        val rhs = production.second.toMutableList()
        rhs.add(dotPosition, "\u2022")
        return "${production.first} := ${rhs.reduce { acc, s -> "$acc$s" }}"
    }
}
