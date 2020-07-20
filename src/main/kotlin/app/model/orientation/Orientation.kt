package app.model.orientation

import app.model.Color

/*
    An orientation. Useful to interpret relative movements, or to be saving the position of the cube in the robot's driver.
 */
class Orientation(var colorPositions: HashMap<RelativePosition, Color>) {

    fun getPositionOfColor(color : Color) : RelativePosition
    {
        return colorPositions.filter { it.value == color }.keys.first()
    }

    fun getColorAtPosition(relativePosition: RelativePosition) : Color
    {
        return colorPositions[relativePosition]!!
    }

    override fun equals(other: Any?): Boolean {
        return (other is Orientation && other.colorPositions == colorPositions)
    }

}