package source.command

import source.io.InOutManager

class Exit: Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        println("Завершение")
        return  true
    }

    override fun description(): String = "exit : завершить программу (без сохранения в файл)"
}