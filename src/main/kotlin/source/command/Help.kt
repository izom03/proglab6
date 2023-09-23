package source.command

import source.io.InOutManager

class Help(
    private var commands: HashMap<String, Command>
): Command {

    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        commands.forEach {
                command -> println(command.value.description())
        }
        return  false
    }

    override fun description(): String = "help : вывести справку по доступным командам"
}