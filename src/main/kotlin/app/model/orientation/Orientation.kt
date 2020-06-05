package app.model.orientation

import app.model.Color
import app.model.movement.RelativePosition

class Orientation{

    var colorPositions = HashMap<RelativePosition, Color>()

    fun init()
    {
        colorPositions[RelativePosition.TOP] =
            Color.WHITE
        colorPositions[RelativePosition.LEFT] =
            Color.ORANGE
        colorPositions[RelativePosition.FRONT] =
            Color.GREEN
        colorPositions[RelativePosition.RIGHT] =
            Color.RED
        colorPositions[RelativePosition.BOTTOM] =
            Color.YELLOW
        colorPositions[RelativePosition.BACK] =
            Color.BLUE
        turnCube(RelativePosition.LEFT)
        turnCube(RelativePosition.LEFT)
    }

    fun turnCube(direction: RelativePosition)
    {
        val temp = colorPositions[RelativePosition.FRONT]

        when(direction)
        {
            RelativePosition.RIGHT -> {
                colorPositions[RelativePosition.FRONT] = colorPositions[RelativePosition.LEFT]!!
                colorPositions[RelativePosition.LEFT] = colorPositions[RelativePosition.BACK]!!
                colorPositions[RelativePosition.BACK] = colorPositions[RelativePosition.RIGHT]!!
                colorPositions[RelativePosition.RIGHT] = temp!!
            }
            RelativePosition.LEFT -> {
                colorPositions[RelativePosition.FRONT] = colorPositions[RelativePosition.RIGHT]!!
                colorPositions[RelativePosition.RIGHT] = colorPositions[RelativePosition.BACK]!!
                colorPositions[RelativePosition.BACK] = colorPositions[RelativePosition.LEFT]!!
                colorPositions[RelativePosition.LEFT] = temp!!
            }
            RelativePosition.TOP -> {
                colorPositions[RelativePosition.FRONT] = colorPositions[RelativePosition.BOTTOM]!!
                colorPositions[RelativePosition.BOTTOM] = colorPositions[RelativePosition.BACK]!!
                colorPositions[RelativePosition.BACK] = colorPositions[RelativePosition.TOP]!!
                colorPositions[RelativePosition.TOP] = temp!!
            }
            RelativePosition.BOTTOM -> {
                colorPositions[RelativePosition.FRONT] = colorPositions[RelativePosition.TOP]!!
                colorPositions[RelativePosition.TOP] = colorPositions[RelativePosition.BACK]!!
                colorPositions[RelativePosition.BACK] = colorPositions[RelativePosition.BOTTOM]!!
                colorPositions[RelativePosition.BOTTOM] = temp!!
            }
        }

    }

    fun getPositionOfColor(color : Color) : RelativePosition
    {
        return colorPositions.filter { it.value == color }.keys.first()
    }

}