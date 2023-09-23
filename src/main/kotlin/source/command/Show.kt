package source.command

import source.Flat
import source.io.InOutManager

class Show(
    private var cp: CommandProcessor,
    private var filter: (HashSet<Flat>) -> List<Flat>
) : Command {
    override fun execute(arg:Array<String>?, io: InOutManager): Boolean {
        val hashSet = cp.show() ?: return false
        if (hashSet.size == 0) {
            print("Коллекция пуста.\n")
        } else {
            var out: String = ""
            filter(hashSet).forEach {
                flat ->
                out += "id: ${flat.getId()}, " +
                        "name: ${flat.getName()}, " +
                        "coordinates: (${flat.getCoordinates().getX()}, ${flat.getCoordinates().getY()}), " +
                        "creation date: ${flat.getCreationDate()}, " +
                        "area: ${flat.getArea()}, " +
                        "number of rooms: ${flat.getNumberOfRooms() ?: "not stated"}, " +
                        "furniture: ${if (flat.getFurniture()) "yes" else "no"}, " +
                        "furnish: ${flat.getFurnish()}, " +
                        "transport: ${flat.getTransport() ?: "not stated"}, " +
                        "house: " +
                            "${if (flat.getHouse() == null) "not stated"
                            else "(name: ${flat.getHouse()?.getName()}, year: ${flat.getHouse()?.getYear()}, number of lifts: ${flat.getHouse()?.getNumberOfLifts()})" }\n"
            }
            print(out)
        }
        return  false
    }
    override fun description(): String = "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении"
}