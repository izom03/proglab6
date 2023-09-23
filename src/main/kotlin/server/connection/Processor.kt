package server.connection

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import source.client.sent.Message
import source.client.sent.Type

class Processor {
    fun execute(message : Message, cp : source.command.CommandProcessor, xmlMapper : XmlMapper) : String {
        when(message.type) {
            Type.GET_INFO   ->      return cp.getInfo()
            Type.SHOW       ->      return xmlMapper.writeValueAsString(cp.show())
            Type.REMOVE_BY_ID ->    return cp.removeById(message.id.toInt())
            Type.CLEAR      ->      return cp.clear()
            Type.REMOVE_BY_FURNITURE -> return cp.removeByFurniture(message.furniture)
            Type.SHOW_MAX_BY_FURNISH -> return cp.showMaxByFurnish()
            Type.HAS_ID ->          return (if(cp.hasID(message.id.toInt())) "true!" else "no")
            Type.ADD ->             return cp.add(message.name,message.coordinates,message.creationDate,message.area,message.numberOfRooms,
                message.furniture,message.furnish,message.transport,message.house)
            Type.ADD_ID ->          return cp.add(message.id,message.name,message.coordinates,message.creationDate,message.area,message.numberOfRooms,
                message.furniture,message.furnish,message.transport,message.house)
            Type.ADD_MIN ->         return cp.addIfMin(message.name,message.coordinates,message.creationDate,message.area,message.numberOfRooms,
                message.furniture,message.furnish,message.transport,message.house)
            Type.ADD_MAX ->         return cp.addIfMax(message.name,message.coordinates,message.creationDate,message.area,message.numberOfRooms,
                message.furniture,message.furnish,message.transport,message.house)
        }
    }
}