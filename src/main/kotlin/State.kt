class State(val number: Int, val productions: List<AugmentedProduction>) {
    override fun toString(): String {
        val productionsStr = productions.map { it.toString() }.reduce{ acc, s -> "$acc\n$s" }
        return "State $number:\n$productionsStr\n"
    }
}
