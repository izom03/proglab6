package source.io

import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class FileInOut(
    private var fileName: String
): InOutManager {
    var line: Int = 0
    private var file: Array<String>

    init {
        try {
            file = File(fileName).readLines().toTypedArray()
        }
        catch (e: FileNotFoundException) {
            throw Exception("Файл не был обнаружен.")
        }
    }

    override fun <T> readLine(out: String, convert: (String) -> T): T {
        var result: T
        try {
            result = convert(file[line++])
        }
        catch (e: Exception) {
            throw Exception("${e.message} В строке $line")
        }
        return result
    }
}