import java.io.File

fun main() {
    val grammar = Grammar(File("gr1BNF.txt"), "program")
//    val grammar = Grammar(File("gr1.txt"), "S")
//    val grammar = Grammar(File("seminar_example.txt"), "S")
    val menu = Menu(grammar)

//    val parser = Parser(grammar)
//    parser.closure(grammar.augmentedProductionsForNonTerminal("(start)"))
//    parser.canonicalCollection().forEach { println(it) }
//    val table = parser.createTable()
//    println(table.toString())
//    parser.parse("ca".map{ it.toString() })

//    parser.parse("""
//        {
//            x <- 5 + 7
//        }
//    """.trimIndent().split(" ", "\n").filter{ it.isNotEmpty() })

    menu.display()
}
