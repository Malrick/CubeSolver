package app.utils.maths

import org.koin.core.KoinComponent

class MathUtils : KoinComponent{
    fun factorial(number : Int) : Int
    {
        var factorial: Long = 1
        for (i in 1..number) {
            // factorial = factorial * i;
            factorial *= i.toLong()
        }
        return factorial.toInt()
    }

    fun pfactorial(numberOne : Int, numberTwo : Int) : Int
    {
        return factorial(numberOne) / factorial(numberOne - numberTwo)
    }
}