package server.connection

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import source.Flat
import source.client.sent.Message
import source.client.sent.Type
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketTimeoutException
import java.nio.CharBuffer

class Receiver(
    val cp : source.command.CommandProcessor
) {
    @JsonSerialize
    var message = Message(Type.GET_INFO)
    @JsonSerialize
    var hashSet: HashSet<Flat>? = HashSet()
    private var returns: String = ""
    private val connectionProcessor = Processor()
    private val sender = Sender()

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

    private val receiveArray = ByteArray(1024*1024)
    private var receiveLength = receiveArray.size
    private var sendArray: ByteArray? = null
    private var sendLength: Int? = null
    private var socket = DatagramSocket(5555)
    private var receivePacket = DatagramPacket(receiveArray, receiveLength)
    private var inStream = ByteArrayInputStream(receiveArray)
    private val charBuffer = CharBuffer.allocate(1024*1024)

    init {
        socket.soTimeout = 100
    }

    fun update() {
        try {
            socket.receive(receivePacket)
        }
        catch (e : SocketTimeoutException) {
            return
        }
        InputStreamReader(inStream).read(charBuffer)
        charBuffer.flip()
        val data = charBuffer.toString()
        val host = receivePacket.address
        val port = receivePacket.port

        message = xmlDeserializer.readValue(data, Message::class.java)


        // Обработка полученных данных
        returns = connectionProcessor.execute(message, cp, xmlMapper)


        sendArray = returns.toByteArray()
        sendLength = sendArray!!.size
        sender.send(sendArray, sendLength, socket, host, port, charBuffer, inStream)

    }
}