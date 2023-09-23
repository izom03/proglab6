package server

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import source.*
import java.time.LocalDate
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter

class CommandProcessor : source.command.CommandProcessor {
    class Data(
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        var initDate: LocalDate,
        @JsonSerialize
        var hashSet: HashSet<Flat>?
    ){}
    @JsonSerialize
    private var data: Data

    private val xmlMapper: XmlMapper = XmlMapper(
        JacksonXmlModule().apply { setDefaultUseWrapper(false) }
    ).registerModule(JavaTimeModule()).apply {
        enable(SerializationFeature.INDENT_OUTPUT)
        enable(SerializationFeature.WRAP_ROOT_VALUE)
    }.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) as XmlMapper

    private val xmlDeserializer = XmlMapper(JacksonXmlModule().apply {
        setDefaultUseWrapper(false)
    }).registerKotlinModule().registerModule(JavaTimeModule())
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val fileName = "flat_collection.xml"

    init {
        try {
            val fileString = File(fileName).readText()
            data = xmlDeserializer.readValue(fileString, Data::class.java)
            if(data.hashSet == null)
                data.hashSet = HashSet()
            println("Коллекция загружена")
        }
        catch (e: FileNotFoundException) {
            println("Файл не был обнаружен, коллекция создана.")
            data = Data(LocalDate.now(), HashSet())
        }
        catch (e: Exception) {
            println("Файл невозможно прочесть, коллекция создана.")
            data = Data(LocalDate.now(), HashSet())
        }
    }

    fun save() {
        val xml = xmlMapper.writeValueAsString(data)
        val writer = PrintWriter(fileName)
        writer.append(xml)
        writer.close()
        println("Сохранение завершено.")
    }

    override fun getInfo(): String = "Информация о коллекции:\n" +
            " - Тип коллекции: HashSet\n" +
            " - Дата инициализации: ${data.initDate.toString()}\n" +
            " - Количество элементов: ${data.hashSet!!.size}\n" +
            " - Класс элементов: Flat"

    override fun show(): HashSet<Flat>? {
        return data.hashSet
    }

    override fun removeById(id: Int): String {
        data.hashSet!!.forEach { flat ->
            if (flat.getId() == id.toLong()) {
                data.hashSet!!.remove(flat)
                return "Элемент удалён из коллекции."
            }
        }
        return "Элемент с таким id не обнаружен."
    }

    override fun clear() : String {
        data.hashSet = HashSet()
        return "Коллекция очищена"
    }

    override fun removeByFurniture(furniture: Boolean): String {
        data.hashSet!!.forEach() {
                flat ->
            if(flat.getFurniture() == furniture) {
                data.hashSet!!.remove(flat)
                return "Элемент удалён из коллекции."
            }
        }
        return "Элемента с таким значением furniture не найдено."
    }

    override fun showMaxByFurnish():String {
        var maxFlat: Flat? = null
        if (data.hashSet!!.size == 0)
            return "Коллекция пуста.\n"
        data.hashSet!!.forEach {
                flat ->
            if (maxFlat == null || (maxFlat?.getFurnish()!!.ordinal ?: -1) < flat.getFurnish().ordinal)
                maxFlat = flat
        }
        val flat = maxFlat!!
        return "id: ${flat.getId()}, " +
                "name: ${flat.getName()}, " +
                "coordinates: (${flat.getCoordinates().getX()}, ${flat.getCoordinates().getY()}), " +
                "creation date: ${flat.getCreationDate()}, " +
                "area: ${flat.getArea()}, " +
                "number of rooms: ${flat.getNumberOfRooms() ?: "not stated"}, " +
                "furniture: ${if (flat.getFurniture()) "yes" else "no"}, " +
                "furnish: ${flat.getFurnish()}, " +
                "transport: ${flat.getTransport() ?: "not stated"}, " +
                "house: " +
                "${if (flat.getHouse() == null) "not stated"
                else "(name: ${flat.getHouse()?.getName()}, year: ${flat.getHouse()?.getYear()}, number of lifts: ${flat.getHouse()?.getNumberOfLifts()})" }\n"
    }

    override fun hasID(id: Int): Boolean {
        data.hashSet!!.forEach { flat ->
            if (flat.getId() == id.toLong()) {
                return true
            }
        }
        return false
    }

    override fun add(name: String,
                    coordinates: Coordinates,
                    creationDate: LocalDate,
                    area: Float,
                    numberOfRooms: Int?,
                    furniture: Boolean,
                    furnish: Furnish,
                    transport: Transport?,
                    house: House?
    ) : String {
        var id: Long = 0
        var max: Int = 0
        data.hashSet!!.forEach { flat ->
            max = if(max < flat.getId()) flat.getId().toInt() else max
        }
        val array = arrayOfNulls<Flat>(max + 1)
        data.hashSet!!.forEach { flat ->
            array[flat.getId().toInt()] = flat
        }
        array.forEach {
                flat ->
            if(flat != null)
                id++
            else {
                data.hashSet!!.add(Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, transport, house))
                return "Объект добавлен в коллекцию."
            }
        }
        data.hashSet!!.add(Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, transport, house))
        return "Объект добавлен в коллекцию."
    }

    override fun add(id: Long,
                     name: String,
                     coordinates: Coordinates,
                     creationDate: LocalDate,
                     area: Float,
                     numberOfRooms: Int?,
                     furniture: Boolean,
                     furnish: Furnish,
                     transport: Transport?,
                     house: House?
    ) : String {
        if (!hasID(id.toInt()))
            return "Объект с таким значением ID не найден."
        removeById(id.toInt())
        data.hashSet!!.add(Flat(id, name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, transport, house))
        return "Объект добавлен в коллекцию."
    }

    override fun addIfMax(name: String,
                     coordinates: Coordinates,
                     creationDate: LocalDate,
                     area: Float,
                     numberOfRooms: Int?,
                     furniture: Boolean,
                     furnish: Furnish,
                     transport: Transport?,
                     house: House?
    ) : String {
        if (isMax(area))
            return add(name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, transport, house)
        return "Элемент не бы добавлен в коллекцию по условию."
    }

    override fun addIfMin(name: String,
                          coordinates: Coordinates,
                          creationDate: LocalDate,
                          area: Float,
                          numberOfRooms: Int?,
                          furniture: Boolean,
                          furnish: Furnish,
                          transport: Transport?,
                          house: House?
    ) : String {
        if (isMin(area))
            return add(name, coordinates, creationDate, area, numberOfRooms, furniture, furnish, transport, house)
        return "Элемент не бы добавлен в коллекцию по условию."
    }


    private fun isMax(a: Float): Boolean {
        data.hashSet!!.forEach {
                flat -> if(flat.getArea() >= a)
            return false
        }
        return true
    }

    private fun isMin(a: Float): Boolean {
        data.hashSet!!.forEach {
                flat -> if(flat.getArea() <= a)
            return false
        }
        return true
    }
}