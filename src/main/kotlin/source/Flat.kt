package source

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flat(
    private var id: Long,                   //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private val name: String,               //Поле не может быть null, Строка не может быть пустой
    private val coordinates: Coordinates,   //Поле не может быть null
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private val creationDate: LocalDate,    //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private val area: Float,                //Значение поля должно быть больше 0
    private val numberOfRooms: Int?,        //Поле может быть null, Значение поля должно быть больше 0
    private val furniture: Boolean,
    private val furnish: Furnish,           //Поле не может быть null
    private val transport: Transport?,      //Поле может быть null
    private val house: House?               //Поле может быть null
) {
    fun setId(id: Long) {
        this.id = id
    }
    fun getId() = id
    fun getName() = name
    fun getCoordinates() = coordinates
    fun getCreationDate() = creationDate
    fun getArea() = area
    fun getNumberOfRooms() = numberOfRooms
    fun getFurniture() = furniture
    fun getFurnish() = furnish
    fun getTransport() = transport
    fun getHouse() = house

    override fun hashCode(): Int {
        return id.toInt()
    }
}

public class Coordinates(
    private val x: Float,
    private val y: Long                     //Значение поля должно быть больше -816, Поле не может быть null
) {
    fun getX() = x
    fun getY() = y
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public class House(
    private val name: String?,              //Поле может быть null
    private val year: Long,                 //Значение поля должно быть больше 0
    private val numberOfLifts: Int          //Значение поля должно быть больше 0
) {
    fun getName() = name
    fun getYear() = year
    fun getNumberOfLifts() = numberOfLifts
}

public enum class Furnish {
    FINE, BAD, LITTLE
}

public enum class Transport {
    FEW, NORMAL, ENOUGH
}