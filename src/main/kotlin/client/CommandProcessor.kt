package client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import source.*
import source.client.sent.*
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.DatagramChannel
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.time.LocalDate


class CommandProcessor : source.command.CommandProcessor {
    val serverAddress = "localhost" // Адрес сервера
    val serverPort = 5555 // Порт сервера
    @JsonSerialize
    var message = Message(Type.GET_INFO)
    @JsonSerialize
    var hashSet: HashSet<Flat>? = HashSet()

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

    private val receiveBuffer: ByteBuffer = ByteBuffer.allocate(1024*1024)
    private var sendBuffer: ByteBuffer = ByteBuffer.allocate(0)
    private val inStream = ByteArrayInputStream(receiveBuffer.array())
    private val charBuffer = CharBuffer.allocate(1024*1024)
    private val address = InetSocketAddress(serverAddress, serverPort)
    private val channel = DatagramChannel.open()

    init {
        channel.configureBlocking(false)
    }

    private fun send() : String? {
        var returns = ""
        try {
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

            // Отправка запроса на сервер
            val xml = xmlMapper.writeValueAsString(message)
            sendBuffer = ByteBuffer.wrap(xml.toByteArray())
            channel.send(sendBuffer, address)
            sendBuffer.clear()

            val selector = Selector.open()
            channel.register(selector, SelectionKey.OP_READ)

            val timeout = 10000

            while (true) {
                val selectedKeys = selector.select(timeout.toLong())

                if (selectedKeys == 0) {
                    // Время ожидания истекло, нет данных для получения
                    println("Превышено время ожидания. Нет данных для получения.")
                    return ""
                }

                val iterator = selector.selectedKeys().iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    if (key.isReadable) {
                        channel.receive(receiveBuffer)
                        InputStreamReader(inStream).read(charBuffer)

                        charBuffer.flip()
                        returns = charBuffer.toString()
                        charBuffer.clear()
                        inStream.reset()
                        receiveBuffer.clear()
                        receiveBuffer.put(ByteArray(256*256))
                        receiveBuffer.clear()

                        return returns
                    }
                    iterator.remove()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Ошибка передачи данных"
        }
    }

    override fun getInfo(): String {
        message = Message(Type.GET_INFO)
        return send()!!
    }

    override fun show(): HashSet<Flat>? {
        message = Message(Type.SHOW)
        val typeReference = object : TypeReference<HashSet<Flat>>() {}
        val text = send()
        if(text.isNullOrEmpty())
            return null
        hashSet = xmlDeserializer.readValue(text, typeReference)
        return hashSet
    }

    override fun removeById(id: Int): String {
        message = Message(Type.REMOVE_BY_ID, id.toLong())
        return send()!!
    }

    override fun clear(): String {
        message = Message(Type.CLEAR)
        return send()!!
    }

    override fun removeByFurniture(furniture: Boolean): String {
        message = Message(Type.REMOVE_BY_FURNITURE, furniture = furniture)
        return send()!!
    }

    override fun showMaxByFurnish(): String {
        message = Message(Type.SHOW_MAX_BY_FURNISH)
        return send()!!
    }

    override fun hasID(id: Int): Boolean {
        message = Message(Type.HAS_ID, id.toLong())
        return send()!!.length > 3
    }

    override fun add(
        name: String,
        coordinates: Coordinates,
        creationDate: LocalDate,
        area: Float,
        numberOfRooms: Int?,
        furniture: Boolean,
        furnish: Furnish,
        transport: Transport?,
        house: House?,
    ): String {
        message = Message(Type.ADD, 0,  name,
            coordinates,
            creationDate,
            area,
            numberOfRooms,
            furniture,
            furnish,
            transport,
            house)
        return send()!!
    }

    override fun add(
        id: Long,
        name: String,
        coordinates: Coordinates,
        creationDate: LocalDate,
        area: Float,
        numberOfRooms: Int?,
        furniture: Boolean,
        furnish: Furnish,
        transport: Transport?,
        house: House?,
    ): String {
        message = Message(Type.ADD_ID, id.toLong(),  name,
            coordinates,
            creationDate,
            area,
            numberOfRooms,
            furniture,
            furnish,
            transport,
            house)
        return send()!!
    }

    override fun addIfMax(
        name: String,
        coordinates: Coordinates,
        creationDate: LocalDate,
        area: Float,
        numberOfRooms: Int?,
        furniture: Boolean,
        furnish: Furnish,
        transport: Transport?,
        house: House?,
    ): String {
        message = Message(Type.ADD_MAX, 0,  name,
            coordinates,
            creationDate,
            area,
            numberOfRooms,
            furniture,
            furnish,
            transport,
            house)
        return send()!!
    }

    override fun addIfMin(
        name: String,
        coordinates: Coordinates,
        creationDate: LocalDate,
        area: Float,
        numberOfRooms: Int?,
        furniture: Boolean,
        furnish: Furnish,
        transport: Transport?,
        house: House?,
    ): String {
        message = Message(Type.ADD_MIN, 0,  name,
            coordinates,
            creationDate,
            area,
            numberOfRooms,
            furniture,
            furnish,
            transport,
            house)
        return send()!!
    }
}