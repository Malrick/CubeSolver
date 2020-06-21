package app.model.orientation

import app.model.Color
import app.model.movement.RelativePosition

class Orientation(var colorPositions: HashMap<RelativePosition, Color>) {

    fun getPositionOfColor(color : Color) : RelativePosition
    {
        return colorPositions.filter { it.value == color }.keys.first()
    }

    override fun equals(other: Any?): Boolean {
        return (other is Orientation && other.colorPositions == colorPositions)
    }

}