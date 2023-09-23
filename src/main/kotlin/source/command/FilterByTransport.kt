package source.command

import source.Transport
import source.io.InOutManager

class FilterByTransport(
    private var cp: CommandProcessor
): Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        if (arg.isNullOrEmpty()) {
            Show(cp){ a -> a.toList() }.execute(arg, io)
            return false
        }
        val transport: Transport = when (arg[0]) {
            "few" -> Transport.FEW
            "normal"  -> Transport.NORMAL
            "enough" -> Transport.ENOUGH
            else  -> {
                throw Exception("Ошибка: Аргумент указан неверно.")
            }
        }
        Show(cp){ a -> a.filter{(it.getTransport()?.ordinal ?: -1) >= transport.ordinal}
        }.execute(arg, io)
        return  false
    }

    override fun description(): String = "filter_greater_than_transport {\"few\", \"normal\", \"enough\" или ничего} : вывести элементы, значение поля transport которых больше заданного"
}