package source.command

import source.*
import java.time.LocalDate

interface CommandProcessor {
   fun getInfo() : String

   fun show() : HashSet<Flat>?

   fun removeById(id: Int) : String

   fun clear() : String

   fun removeByFurniture(furniture: Boolean) : String

   fun showMaxByFurnish() : String

   fun hasID(id: Int) : Boolean

   fun add(name: String,
           coordinates: Coordinates,
           creationDate: LocalDate,
           area: Float,
           numberOfRooms: Int?,
           furniture: Boolean,
           furnish: Furnish,
           transport: Transport?,
           house: House?
   ) : String

    fun add(id: Long,
            name: String,
            coordinates: Coordinates,
            creationDate: LocalDate,
            area: Float,
            numberOfRooms: Int?,
            furniture: Boolean,
            furnish: Furnish,
            transport: Transport?,
            house: House?
    ) : String

    fun addIfMax(name: String,
            coordinates: Coordinates,
            creationDate: LocalDate,
            area: Float,
            numberOfRooms: Int?,
            furniture: Boolean,
            furnish: Furnish,
            transport: Transport?,
            house: House?
    ) : String

    fun addIfMin(name: String,
            coordinates: Coordinates,
            creationDate: LocalDate,
            area: Float,
            numberOfRooms: Int?,
            furniture: Boolean,
            furnish: Furnish,
            transport: Transport?,
            house: House?
    ) : String
}