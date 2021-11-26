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
}
