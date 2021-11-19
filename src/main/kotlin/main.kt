import java.io.File

fun main() {
    val grammar = Grammar(File("gr1.txt"))
    val menu = Menu(grammar)

    menu.display()
}
