
private const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_BAK_BLACK = "\u001B[40m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_BAK_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_BAK_GREEN = "\u001B[42m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BAK_YELLOW = "\u001B[43m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_BAK_BLUE = "\u001B[44m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_BAK_PURPLE = "\u001B[45m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_BAK_CYAN = "\u001B[46m"
const val ANSI_WHITE = "\u001B[37m"
const val ANSI_BAK_WHITE = "\u001B[47m"


fun println(color: String, text: String) {
    printlnColor("$color$text")
}


fun print(color: String, text: String) {
    printColor("$color$text")
}

fun printColor(text: String) {
    print("$text$ANSI_RESET")
}

fun printlnColor(text: String) {
    println("$text$ANSI_RESET")
}