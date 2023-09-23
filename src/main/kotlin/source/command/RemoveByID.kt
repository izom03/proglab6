package source.command


import source.io.InOutManager
import java.lang.Exception

class RemoveByID(
    private var cp: CommandProcessor
) : Command {
    override fun execute(arg: Array<String>?, io: InOutManager): Boolean {
        try {
            print(cp.removeById(arg!![0].toInt()))
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат числа.")
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw Exception("Ошибка: Команда требует заполненного аргумента.")
        }
        return  false
    }

    override fun description(): String =
        "remove_by_id {id} : удалить элемент из коллекции по его id"
}