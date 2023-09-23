package source.command

import source.io.InOutManager

class MaxByFurnish(
    private var cp: CommandProcessor
): Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        print(cp.showMaxByFurnish())
        return  false
    }

    override fun description(): String = "max_by_furnish : вывести любой объект из коллекции, значение поля furnish которого является максимальным"
}