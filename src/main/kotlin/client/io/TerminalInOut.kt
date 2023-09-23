package client.io

import source.io.InOutManager
import java.lang.Exception

class TerminalInOut: InOutManager {
    override fun <T> readLine(out: String, convert: (String) -> T): T {
        var result: T
        while (true)
            try {
                print(out)
                result = convert(readLine().toString())
                break
            }
            catch (e: Exception) {
                println(e.message + " Повторите попытку.")
            }
        return result
    }
}