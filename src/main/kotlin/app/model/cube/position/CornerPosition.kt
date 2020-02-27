package app.model.cube.position

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Corner
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class CornerPosition : Position
{
    var colorOne : Color
    var colorTwo : Color
    var colorThree : Color

    constructor(cubeCoordinates: CubeCoordinates, colorOne : Color, colorTwo : Color, colorThree : Color)
    {
        this.cubeCoordinates = cubeCoordinates
        this.colorOne = colorOne
        this.colorTwo = colorTwo
        this.colorThree = colorThree
    }

    override fun matches(piece : Piece) : Boolean
    {
        if(piece !is Corner) throw UnsupportedOperationException()
        else return (piece.colorOne == colorOne && piece.colorTwo == colorTwo && piece.colorThree == colorThree)
    }

    override fun possessColor(color: Color): Boolean {
        return (colorOne == color || colorTwo == color || colorThree == color)
    }

    override fun positionOfColor(color: Color): Int {
        return when (color) {
            colorOne -> 1
            colorTwo -> 2
            colorThree -> 3
            else -> 0
        }
    }
}