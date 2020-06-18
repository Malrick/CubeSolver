package app.utils.indexing

import app.utils.maths.MathUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class LehmerRanker : KoinComponent {

    private val mathUtils : MathUtils by inject()

    private var lookUpTableCornerOrientation = HashMap<String, Int>()
    private var lookUpTableCornerPosition = HashMap<String, Int>()
    private var lookUpTableEdgeOrientation = HashMap<String, Int>()
    private var lookUpTableEdgePosition = HashMap<String, Int>()

    fun lookupCornersOrientation(sequence : Array<Int>) : Int
    {
        var result = lookUpTableCornerOrientation[sequence.joinToString()]

        if(result == null)
        {
            var ranking = this.rankWithReplacement(sequence, 2)
            lookUpTableCornerOrientation[sequence.joinToString()] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }
    fun lookupEdgesOrientation(sequence : Array<Int>) : Int
    {
        var result = lookUpTableEdgeOrientation[sequence.joinToString()]

        if(result == null)
        {
            var ranking = this.rankWithReplacement(sequence, 2)
            lookUpTableEdgeOrientation[sequence.joinToString()] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }

    fun lookupCornersPosition(sequence : Array<Int>) : Int
    {
        var result = lookUpTableCornerPosition[sequence.joinToString()]

        if(result == null)
        {
            var ranking = this.rankWithoutReplacement(sequence, 12, false)
            lookUpTableCornerPosition[sequence.joinToString()] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }

    fun lookupEdgesPosition(sequence : Array<Int>) : Int
    {
        var result = lookUpTableCornerOrientation[sequence.joinToString()]

        if(result == null)
        {
            var ranking = this.rankWithoutReplacement(sequence, 12, true)
            lookUpTableEdgePosition[sequence.joinToString()] = ranking
            return ranking
        }
        else
        {
            return result
        }

    }

    private fun rankWithReplacement(sequence : Array<Int>, maxElement : Int) : Int
    {
        var toReturn = 0
        var multiplicator = 1
        for(indice in sequence.size-1 downTo 0)
        {
            toReturn += multiplicator * sequence[indice]
            multiplicator *= 3
        }
        return toReturn
    }

    private fun rankWithoutReplacement(sequence : Array<Int>, maxElement : Int, partialPermutations : Boolean) : Int
    {
        var bitSet = Array(maxElement) {0}
        var array = arrayOf<Int>()
        var multiplicator : Int
        var result = 0

        // Creating Lehmer Rank
        for(number in sequence)
        {
            bitSet[number] = 1
            array += (number - bitSet.slice(IntRange(0, number-1)).count { it ==1 })
        }

        // Converting to base 10
        for(i in 0 until array.size)
        {
            if(partialPermutations)
            {
                multiplicator= mathUtils.pfactorial(maxElement - 1 - i, array.size-1-i)
            }
            else
            {
                multiplicator = mathUtils.factorial(i)
            }
            var elem = array[i]
            result += multiplicator * elem
        }

        return result
    }


}