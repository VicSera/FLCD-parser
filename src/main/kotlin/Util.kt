fun Pair<String, List<String>>.equalsProduction(other: Pair<String, List<String>>): Boolean {
    return this.first == other.first && this.second.checkEqual(other.second)
}

fun <T> List<T>.contains(other: List<T>): Boolean {
    return other.find { e1 -> this.find { e2 -> e1?.equals(e2) ?: false } == null } == null
}

fun <T> List<T>.checkEqual(other: List<T>): Boolean {
    return this.contains(other) && other.contains(this)
}
