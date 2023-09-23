package source.client.sent

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import source.Coordinates
import source.Furnish
import source.House
import source.Transport
import java.time.LocalDate

data class Message(
    val type: Type,
    val id: Long                    = 0,
    val name: String                = "",
    val coordinates: Coordinates    = Coordinates(0F, 0),
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    val creationDate: LocalDate     = LocalDate.now(),
    val area: Float                 = 0F,
    val numberOfRooms: Int?         = null,
    val furniture: Boolean          = true,
    val furnish: Furnish            = Furnish.BAD,
    val transport: Transport?       = null,
    val house: House?               = null)