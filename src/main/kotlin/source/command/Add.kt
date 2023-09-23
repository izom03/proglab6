package source.command

import source.io.InOutManager
import source.Coordinates
import source.Furnish
import source.House
import source.Transport
import java.time.LocalDate

open class Add (
    private var cp: CommandProcessor,
    private var descriptionField: String,
    private var command: (String, Coordinates, LocalDate, Float, Int?, Boolean, Furnish, Transport?, House?) -> String
): Command {
    private fun readCoordinates(arg: String): Coordinates {
        val userInput: Array<String> = arg.split(" ").toTypedArray()
        val x: Float
        val y: Long
        if (userInput.size != 2) {
            throw Exception("Ошибка: Неверное количество аргументов.")
        }
        try {
            x = userInput[0].toFloat()
            y = userInput[1].toLong()
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат введённого числа.")
        }
        if (y <= -816) {
            throw Exception("Ошибка: Значение второго числа должно быть больше -816.")
        }
        return Coordinates(x, y)
    }

    private fun readArea(arg: String): Float {
        val area: Float
        try {
            area = arg.toFloat()
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат введённого числа.")
        }
        if (area <= 0)
            throw Exception("Ошибка: Число должно быть больше 0.")
        return area
    }

    private fun readNumberOfRooms(arg: String): Int? {
        val numberOfRooms: Int?
        if (arg == "") {
            return null
        }
        try {
            numberOfRooms = arg.toInt()
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат введённого числа.")
        }
        return numberOfRooms
    }

    private fun readFurniture(arg: String): Boolean {
        val furniture: Boolean = when (arg) {
            "yes" -> true
            "no"  -> false
            else  -> {
                throw Exception("Ошибка: Введено неверное слово.")
            }
        }
        return furniture
    }

    private fun readFurnish(arg: String): Furnish {
        val furnish: Furnish = when (arg) {
            "fine"   -> Furnish.FINE
            "bad"    -> Furnish.BAD
            "little" -> Furnish.LITTLE
            else  -> {
                throw Exception("Ошибка: Введено неверное слово.")
            }
        }
        return furnish
    }

    private fun readTransport(arg: String): Transport? {
        val transport: Transport? = when (arg) {
            ""       -> null
            "few"    -> Transport.FEW
            "normal" -> Transport.NORMAL
            "enough" -> Transport.ENOUGH
            else  -> {
                throw Exception("Ошибка: Введено неверное слово.")
            }
        }
        return transport
    }

    private fun readYear(arg: String): Long {
        val year: Long
        try {
            year = arg.toLong()
            if (year <= 0)
                throw Exception("Ошибка: Значение должно быть положительным.")
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат числа.")
        }
        return year
    }

    private fun readNumberOfLifts(arg: String): Int {
        val numberOfLifts: Int
        try {
            numberOfLifts = arg.toInt()
            if (numberOfLifts <= 0)
                throw Exception("Ошибка: Значение должно быть положительным.")
        } catch (e: NumberFormatException) {
            throw Exception("Ошибка: Неверный формат числа.")
        }
        return numberOfLifts
    }

    private fun readHouse(arg: String, io: InOutManager): House? {
        val house: House? = when (arg) {
            "yes" -> {
                val houseName: String? = io.readLine<String?>("Введите название: ") {str -> str}
                val year: Long = io.readLine<Long>("Ведите год постройки: ") {str -> readYear(str)}
                val numberOfLifts: Int = io.readLine<Int>("Укажите количество лифтов в здании: ") {str -> readNumberOfLifts(str)}
                House(if (houseName == "") null else houseName, year, numberOfLifts)
            }
            "no"  -> null
            else  -> {
                throw Exception("Ошибка: Введено неверное слово.")
            }
        }
        return house
    }

    override fun execute(arg: Array<String>?, io: InOutManager): Boolean {
        if (arg.isNullOrEmpty())
            throw Exception("Ошибка: Не указано название.")
        var name: String = ""
        arg.forEach { a ->
            name += "$a "
        }
        name = name.substring(0, name.length - 1)

        try {
            val coordinates: Coordinates =
                io.readLine<Coordinates>("Введите координаты (число с плавающей точкой и целое число, разделённые пробелом): ") { str ->
                    readCoordinates(str)
                }
            val area: Float = io.readLine<Float>("Введите площадь: ") { str -> readArea(str) }
            val numberOfRooms: Int? =
                io.readLine<Int?>("Введите число комнат (Enter чтобы пропустить): ") { str -> readNumberOfRooms(str) }
            val furniture: Boolean =
                io.readLine<Boolean>("Есть ли в квартире мебель? (\"yes\" или \"no\"): ") { str -> readFurniture(str) }
            val furnish: Furnish =
                io.readLine<Furnish>("Какая в квартире отделка? (\"fine\", \"bad\", \"little\"): ") { str ->
                    readFurnish(str)
                }
            val transport: Transport? =
                io.readLine<Transport?>("Как в области развит общественный транспорт? (\"few\", \"normal\", \"enough\" или Enter чтобы пропустить): ") { str ->
                    readTransport(str)
                }
            val house: House? =
                io.readLine<House?>("Хотите добавить информацию о доме? (\"yes\" или \"no\"): ") { str ->
                    readHouse(
                        str,
                        io
                    )
                }
            println(command(name, coordinates, LocalDate.now(), area, numberOfRooms, furniture, furnish, transport, house))
        }
        catch (e: Exception) {
            throw e
        }

        return false
    }

    override fun description():String = descriptionField
}