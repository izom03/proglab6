package source.io

interface InOutManager {
    fun <T> readLine(out: String, convert: (String) ->  T): T
}