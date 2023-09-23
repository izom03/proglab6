package source.command

import source.io.InOutManager

class Clear(
    private var cp: CommandProcessor
) : Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        print(cp.clear())
        return  false
    }

    override fun description(): String = "clear : очистить коллекцию"
}