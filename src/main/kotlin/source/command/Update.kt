package source.command

import source.io.InOutManager
import java.lang.Exception

class Update(
    private var cp: CommandProcessor
) : Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        try {
            if(!cp.hasID(arg!![0].toInt())) {
                println("Элемент с таким id не обнаружен.")
                return false
            }
            Add(cp, "add {название} : добавить новый элемент в коллекцию") { a,b,c,d,e,f,g,h,i -> cp.add(arg!![0].toLong(),a,b,c,d,e,f,g,h,i)}.execute(
                arrayOf(io.readLine<String>("Введите название: ") {str -> str}), io)
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат числа.")
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw Exception("Ошибка: Команда требует заполненного аргумента.")
        }
        return  false
    }

    override fun description(): String = "update {id} : обновить значение элемента коллекции, id которого равен заданному"
}