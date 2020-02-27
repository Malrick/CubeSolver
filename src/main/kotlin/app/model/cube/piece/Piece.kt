package app.model.cube.piece

import app.model.cubeUtils.Color

abstract class Piece {

    abstract fun getColorAtPosition(positionOfColor : Int) : Color?

    abstract fun setColorAtPosition(positionOfColor : Int, color : Color)

    abstract fun containsColor(color : Color) : Boolean

    abstract fun clone() : Piece

}