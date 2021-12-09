import java.io.File

fun main() {
//    val grammar = Grammar(File("gr1BNF.txt"), "program")
    val grammar = Grammar(File("gr1.txt"), "S")
//    val menu = Menu(grammar)

    val parser = Parser(grammar)
//    parser.closure(grammar.augmentedProductionsForNonTerminal("(start)"))
    val cc = parser.canonicalCollection()
    cc.map { it.toString() }.forEach { println(it) }

//    menu.display()
}
