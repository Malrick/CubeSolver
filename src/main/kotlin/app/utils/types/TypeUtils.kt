package app.utils.types

class TypeUtils {

    fun arrayToString(array : Array<Int>) : String
    {
        var result = ""

        for(elem in array)
        {
            result+=elem.toString()
        }
        return result
    }
}