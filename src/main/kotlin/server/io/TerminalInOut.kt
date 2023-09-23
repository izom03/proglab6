package server.io

import server.connection.Receiver
import source.io.InOutManager
import java.lang.Exception

class TerminalInOut(val cm : Receiver): InOutManager {
    override fun <T> readLine(out: String, convert: (String) -> T): T {
        var result: T
        print(out)
        while (true)
            try {
                while(System.`in`.available() == 0) {
                    cm.update()
                }
                result = convert(readLine().toString())
                break
            }
            catch (e: Exception) {
                println(e.message + " Повторите попытку.")
            }
        return result
    }
}