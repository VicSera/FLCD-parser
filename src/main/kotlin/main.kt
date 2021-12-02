import java.io.File

fun main() {
//    val grammar = Grammar(File("gr1BNF.txt"))
    val grammar = Grammar(File("gr1.txt"))
//    val menu = Menu(grammar)

    val parser = Parser(grammar)
//    parser.closure(grammar.augmentedProductionsForNonTerminal("(start)"))
    parser.canonicalCollection()

//    menu.display()
}
