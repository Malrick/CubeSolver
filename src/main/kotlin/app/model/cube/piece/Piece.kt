package app.model.cube.piece

import app.model.Color

abstract class Piece {

    abstract fun getColorAtPosition(positionOfColor : Int) : Color?

    abstract fun setColorAtPosition(positionOfColor : Int, color : Color)

    abstract fun containsColor(color : Color) : Boolean

    abstract fun getColors() : Set<Color>

    abstract fun clone() : Piece

}