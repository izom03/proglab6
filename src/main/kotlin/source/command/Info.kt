package source.command

import source.io.InOutManager

class Info(
    private var cp: CommandProcessor
) : Command {
    /**
     * Функция с содержимым команды.
     * @param arg Передаваемые аргументы.
     * @param io Объект, который может быть использован для связи с пользователем.
     * @return Возвращает false
     */
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        println(cp.getInfo())
        return  false
    }

    /**
     * Функция с описанием команды
     */
    override fun description(): String = "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"
}