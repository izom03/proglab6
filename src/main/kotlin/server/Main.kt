package server

import server.connection.Receiver
import server.io.TerminalInOut
import source.command.*

fun addToHistory(arr: Array<String?>, command: String) {
    arr[4] = arr[3]
    arr[3] = arr[2]
    arr[2] = arr[1]
    arr[1] = arr[0]
    arr[0] = command
}

fun main() {
    val history: Array<String?> = arrayOfNulls<String>(5)
    val commands = LinkedHashMap<String, Command>()
    var command: Command?
    var userInput: String
    var splitInput: Array<String>
    val cp = CommandProcessor()
    val cm = Receiver(cp)
    val io = TerminalInOut(cm)
    commands["help"]            = Help(commands)
    commands["info"]            = Info(cp)
    commands["show"]            = Show(cp) { a -> a.toList() }
    commands["add"]             = Add(cp, "add {название} : добавить новый элемент в коллекцию") { a,b,c,d,e,f,g,h,i -> cp.add(a,b,c,d,e,f,g,h,i)}
    commands["update"]          = Update(cp)
    commands["clear"]           = Clear(cp)
    commands["save"]            = Save(cp)
    commands["execute_script"]  = ExecuteScript(commands)
    commands["exit"]            = Exit()
    commands["add_if_max"]      = Add(cp,
        "add_if_max {название} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции")
    { a,b,c,d,e,f,g,h,i -> cp.addIfMax(a,b,c,d,e,f,g,h,i) }
    commands["add_if_min"]      = Add(cp,
        "add_if_min {название} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции")
    { a,b,c,d,e,f,g,h,i -> cp.addIfMin(a,b,c,d,e,f,g,h,i) }
    commands["history"]         = History(history)
    commands["remove_by_id"]         = RemoveByID(cp)
    commands["remove_any_by_furniture"]         = RemoveByFurniture(cp)
    commands["max_by_furnish"]                  = MaxByFurnish(cp)
    commands["filter_greater_than_transport"]   = FilterByTransport(cp)
    while (true) {
        if(System.`in`.available() > 0) {
            userInput = readln()
            splitInput = userInput.split(" ").toTypedArray()
            command = commands[splitInput[0]];
            try {
                if (command!!.execute(splitInput.copyOfRange(1, splitInput.size), io))
                    break
                addToHistory(history, splitInput[0])
            } catch (e: NullPointerException) {
                if (splitInput[0] != "")
                    println("Команда введена неверно. Введите \"help\" для вывода списка команд.")
            } catch (e: Exception) {
                println(e.message)
            }
        }
        cm.update()
    }
}