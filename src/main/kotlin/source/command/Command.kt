package source.command

import source.io.InOutManager

interface Command {
    fun execute(arg:Array<String>?, io: InOutManager): Boolean

    fun description(): String
}