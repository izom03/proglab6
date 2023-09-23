package source.command

import source.io.InOutManager

class RemoveByFurniture(
    private var cp: CommandProcessor
): Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        if (arg.isNullOrEmpty())
            throw Exception("Ошибка: Не указан аргумент.")
        val furniture: Boolean = when (arg[0]) {
            "yes" -> true
            "no"  -> false
            else  -> {
                throw Exception("Ошибка: Аргумент указан неверно.")
            }
        }
        print(cp.removeByFurniture(furniture))
        return  false
    }

    override fun description(): String = "remove_any_by_furniture {\"yes\" или \"no\"} : удалить из коллекции один элемент, значение поля furniture которого эквивалентно заданному"
}