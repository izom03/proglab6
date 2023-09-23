package source.command

import source.io.InOutManager
import server.CommandProcessor

class Save(
    private var cp: CommandProcessor
) : Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        cp.save()
        return  false
    }

    override fun description(): String = "save : сохранить коллекцию в файл"
}