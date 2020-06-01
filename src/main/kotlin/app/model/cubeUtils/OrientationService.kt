package app.model.cubeUtils

class OrientationService {

    fun getOrientations(constraintOne : Pair<RelativePosition, Color>) : List<HashMap<RelativePosition, Color>>
    {
        var toReturn = listOf<HashMap<RelativePosition, Color>>()

        for(color in adjacentColors(constraintOne.second))
        {
            toReturn = toReturn + getOrientation(constraintOne, Pair(adjacentPositions(constraintOne.first).first(), color))
        }

        return toReturn
    }

    fun getOrientation(constraintOne : Pair<RelativePosition, Color>, constraintTwo : Pair<RelativePosition, Color>) : HashMap<RelativePosition, Color> {
        val toReturn = HashMap<RelativePosition, Color>()
        toReturn[constraintOne.first] = constraintOne.second
        toReturn[opposite(constraintOne.first)] = opposite(constraintOne.second)
        toReturn[constraintTwo.first] = constraintTwo.second
        toReturn[opposite(constraintTwo.first)] = opposite(constraintTwo.second)

        val lastPositions = RelativePosition.values().filterNot { setOf(constraintOne.first, opposite(constraintOne.first), constraintTwo.first, opposite(constraintTwo.first)).contains(it) }
        val lastColors = Color.values().filterNot { setOf(constraintOne.second, opposite(constraintOne.second), constraintTwo.second, opposite(constraintTwo.second)).contains(it) }

        toReturn[lastPositions[0]] = lastColors[0]
        toReturn[opposite(lastPositions[0])] = opposite((lastColors[0]))

        if(!integrityCheck(toReturn))
        {
            toReturn[opposite(lastPositions[0])] = lastColors[0]
            toReturn[lastPositions[0]] = opposite((lastColors[0]))
        }

        return toReturn
    }

    fun integrityCheck(toCheck : HashMap<RelativePosition, Color>) : Boolean
    {
        var topBottom = toCheck.filter { (it.key == RelativePosition.BOTTOM || it.key == RelativePosition.TOP) && (it.value == Color.BLUE || it.value == Color.WHITE || it.value == Color.RED)}.toList().first()
        var leftRight = toCheck.filter { (it.key == RelativePosition.LEFT || it.key == RelativePosition.RIGHT) && (it.value == Color.BLUE || it.value == Color.WHITE || it.value == Color.RED)}.toList().first()
        var frontBack = toCheck.filter { (it.key == RelativePosition.FRONT || it.key == RelativePosition.BACK) && (it.value == Color.BLUE || it.value == Color.WHITE || it.value == Color.RED)}.toList().first()

        val setOfBlueWhiteRedPosition = setOf(topBottom.first, leftRight.first, frontBack.first)
        var blueWhiteRedClockwiseArray = arrayOf<Color>()

        when(setOfBlueWhiteRedPosition)
        {
            setOf(RelativePosition.TOP, RelativePosition.FRONT, RelativePosition.LEFT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, frontBack.second, leftRight.second)
            setOf(RelativePosition.TOP, RelativePosition.BACK, RelativePosition.LEFT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, leftRight.second, frontBack.second)
            setOf(RelativePosition.TOP, RelativePosition.BACK, RelativePosition.RIGHT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, frontBack.second, leftRight.second)
            setOf(RelativePosition.TOP, RelativePosition.FRONT, RelativePosition.RIGHT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, leftRight.second, frontBack.second)

            setOf(RelativePosition.BOTTOM, RelativePosition.FRONT, RelativePosition.LEFT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, leftRight.second, frontBack.second)
            setOf(RelativePosition.BOTTOM, RelativePosition.BACK, RelativePosition.LEFT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, frontBack.second, leftRight.second)
            setOf(RelativePosition.BOTTOM, RelativePosition.BACK, RelativePosition.RIGHT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, leftRight.second, frontBack.second)
            setOf(RelativePosition.BOTTOM, RelativePosition.FRONT, RelativePosition.RIGHT) -> blueWhiteRedClockwiseArray = arrayOf(topBottom.second, frontBack.second, leftRight.second)
        }

        when(blueWhiteRedClockwiseArray[0])
        {
            Color.BLUE -> if(blueWhiteRedClockwiseArray[1] == Color.WHITE) return false
            Color.WHITE -> if(blueWhiteRedClockwiseArray[1] == Color.RED) return false
            Color.RED -> if(blueWhiteRedClockwiseArray[1] == Color.BLUE) return false
        }

        return true
    }

    fun convertSequenceOfRelativeMovements(relativeSequence : Array<RelativeMovement>, orientation : HashMap<RelativePosition, Color>) : Array<Movement>
    {
        var toReturn = arrayOf<Movement>()

        for(relativeMovement in relativeSequence)
        {
            var relativeMovementPosition = RelativePosition.values().first { relativeMovement.name.startsWith(it.name) }
            var clockwise = !relativeMovement.name.endsWith("REVERSE")

            var colorOfMovement = orientation[relativeMovementPosition]!!

            var movement : Movement
            if(clockwise) movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE") }
            else movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE") }

            toReturn = toReturn.plus(movement)
        }

        return toReturn
    }

    private fun opposite(relativePosition: RelativePosition) : RelativePosition
    {
        return when(relativePosition)
        {
            RelativePosition.TOP -> RelativePosition.BOTTOM
            RelativePosition.FRONT -> RelativePosition.BACK
            RelativePosition.LEFT -> RelativePosition.RIGHT
            RelativePosition.BOTTOM -> RelativePosition.TOP
            RelativePosition.RIGHT -> RelativePosition.LEFT
            RelativePosition.BACK -> RelativePosition.FRONT
        }
    }

    private fun opposite(color: Color) : Color
    {
        return when(color)
        {
            Color.WHITE -> Color.YELLOW
            Color.ORANGE -> Color.RED
            Color.GREEN -> Color.BLUE
            Color.RED -> Color.ORANGE
            Color.YELLOW -> Color.WHITE
            Color.BLUE -> Color.GREEN
        }
    }

    private fun adjacentColors(color : Color) : Set<Color>
    {
        return Color.values().filter { it != color && it != opposite(color) }.toSet()
    }

    private fun adjacentPositions(relativePosition: RelativePosition) : Set<RelativePosition>
    {
        return RelativePosition.values().filter { it != relativePosition && it != opposite(relativePosition) }.toSet()
    }

}