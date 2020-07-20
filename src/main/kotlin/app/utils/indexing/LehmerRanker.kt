package app.utils.indexing

import app.model.cube.Cube
import app.model.cube.position.CornerPosition
import app.model.cube.position.Position
import app.service.cube.CubeInformationService
import app.utils.maths.MathUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class LehmerRanker : KoinComponent {

    private val mathUtils : MathUtils by inject()

    private var lookUpTableCornerOrientation = HashMap<String, Int>()
    private var lookUpTableCornerPosition = HashMap<String, Int>()
    private var lookUpTableEdgeOrientation = HashMap<String, Int>()
    private var lookUpTableEdgePosition = HashMap<String, Int>()

    private val cubeInformationService : CubeInformationService by inject()

    /*
        Return the full Lehmer rank of the cube's corners
        = corner position lehmper * 3^7 + corner orientation lehmer
     */
    fun lookupFullCornerRank(cube : Cube) : Int
    {
        var sortedCornerPositions = cube.positions.keys.filter { it is CornerPosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending { it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height }
        var cornerOrientations = cubeInformationService.getCornerOrientations(cube)
        var cornerOrientationArray = arrayOf<Int>()
        var cornerPosition = arrayOf<Int>()

        for (corner in sortedCornerPositions!!) {
            var index = sortedCornerPositions!!.indexOfFirst { it.identity == cube.positions[corner]!!.identity}
            cornerOrientationArray += cornerOrientations[index]
            cornerPosition += index
        }


        var toReturn =lookupCornersPosition(cornerPosition) * Math.pow(3.0, 7.0).toInt() + lookupCornersOrientation(cornerOrientationArray)
        return toReturn
    }

    /*
        Lookup corner position's lehmer of a cube
     */
    fun lookupCornersPosition(cube : Cube) : Int
    {
        var cornerPosition = arrayOf<Int>()
        for (corner in cube.lookUps.sortedCornersPositionLookup) {
            cornerPosition += cube.lookUps.sortedCornersPositionLookup!!.indexOfFirst { it.identity == cube.positions[corner]!!.identity}
        }
        return lookupCornersPosition(cornerPosition)
    }

    /*
        Lookup corner orientation's lehmer
        If not present in hashmap, calculate and add it
     */
    fun lookupCornersOrientation(sequence : Array<Int>) : Int
    {
        var stringSequence = sequence.joinToString()
        var result = lookUpTableCornerOrientation[stringSequence]

        if(result == null)
        {
            var ranking = this.rankWithReplacement(sequence, 2)
            lookUpTableCornerOrientation[stringSequence] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }

    /*
        Lookup edge orientation's lehmer
        If not present in hashmap, calculate and add it
     */
    fun lookupEdgesOrientation(sequence : Array<Int>) : Int
    {
        var stringSequence = sequence.joinToString()
        var result = lookUpTableEdgeOrientation[stringSequence]

        if(result == null)
        {
            var ranking = this.rankWithReplacement(sequence, 2)
            lookUpTableEdgeOrientation[stringSequence] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }


    /*
            Lookup corner position's lehmer
            If not present in hashmap, calculate and add it
         */
    fun lookupCornersPosition(sequence : Array<Int>) : Int
    {
        var stringSequence = sequence.joinToString()
        var result = lookUpTableCornerPosition[stringSequence]

        if(result == null)
        {
            var ranking = this.rankWithoutReplacement(sequence, 7, false)
            lookUpTableCornerPosition[stringSequence] = ranking
            return ranking
        }
        else
        {
            return result
        }
    }

    fun lookupEdgesPosition(sequence : Array<Int>) : Int
    {
        var stringSequence = sequence.joinToString()
        var result = lookUpTableCornerOrientation[stringSequence]

        if(result == null)
        {
            var ranking = this.rankWithoutReplacement(sequence, 12, true)
            lookUpTableEdgePosition[stringSequence] = ranking
            return ranking
        }
        else
        {
            return result
        }

    }

    /*
        Calculate the lehmer rank of an element, considering the maximum element possible, and the fact that an element may have many occurences
     */
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

    /*
        Calculating the lehmer rank of a sequence, knowing an element can't occur more than once
     */
    fun rankWithoutReplacement(sequence : Array<Int>, maxElement : Int, partialPermutations : Boolean) : Int
    {
        var bitSet = Array(maxElement+1) {0}
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
        for(i in array.indices)
        {
            if(partialPermutations)
            {
                multiplicator= mathUtils.pfactorial(maxElement - 1 - i, array.size-1-i)
            }
            else
            {
                multiplicator = mathUtils.factorial(i)
            }
            var elem = array[array.size-i-1]
            result += multiplicator * elem
        }

        return result
    }


}