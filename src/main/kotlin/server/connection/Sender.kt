package server.connection

import java.io.ByteArrayInputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.CharBuffer

class Sender {
    fun send(
        sendArray: ByteArray?,
        sendLength: Int?,
        socket: DatagramSocket,
        host: InetAddress,
        port: Int,
        charBuffer: CharBuffer,
        inStream: ByteArrayInputStream,
    ) {
        socket.send(DatagramPacket(sendArray, sendLength!!, host, port))
        charBuffer.clear()
        inStream.reset()
    }

}