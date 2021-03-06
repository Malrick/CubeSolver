package app.model.cube.piece

import app.model.Color

abstract class Piece {

    abstract var identity : String

    abstract fun getColorAtPosition(positionOfColor : Int) : Color?

    abstract fun setColorAtPosition(positionOfColor : Int, color : Color)

    abstract fun getPositionOfColor(color : Color) : Int?

    abstract fun possessColor(color : Color) : Boolean

    abstract fun getColors() : Set<Color>

    abstract fun clone() : Piece
}