package source.command

import source.io.InOutManager

class History(
    private var history: Array<String?>
): Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        history.forEach {
                command -> if(command != null)println(command)
        }
        return  false
    }

    override fun description(): String = "history : вывести последние 5 команд (без их аргументов)"
}