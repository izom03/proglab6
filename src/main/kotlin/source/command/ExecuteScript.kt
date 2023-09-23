package source.command

import source.io.*

class ExecuteScript(
    private var commands: HashMap<String, Command>
) : Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        if (arg.isNullOrEmpty())
            throw Exception("Ошибка: Не указан путь к файлу.")
        var way: String = ""
        arg.forEach { a ->
            way += "$a "
        }
        way = way.substring(0, way.length - 1)
        val io: InOutManager
        try {
            io = FileInOut(way)
        }
        catch (e: Exception) {
            throw e
        }
        while(true) {
            val input = io.readLine("") { str -> str.split(" ").toTypedArray() }
            val command = commands[input[0]] ?: throw Exception("Неверно введена команда в строке ${io.line}.");
            try {
                if (command.execute(input.copyOfRange(1, input.size), io))
                    return true
            }
            catch (e: IndexOutOfBoundsException) {
                println("Выполнение завершено")
                return false
            }
            catch (e: Exception) {
                throw e
            }
        }
    }

    override fun description(): String = "execute_script {путь к файлу} : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."
}