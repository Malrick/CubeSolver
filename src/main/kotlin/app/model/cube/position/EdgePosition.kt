package app.model.cube.position

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class EdgePosition : Position
{
    var colorOne : Color
    var colorTwo : Color

    constructor(cubeCoordinates: CubeCoordinates, colorOne : Color, colorTwo : Color)
    {
        this.cubeCoordinates = cubeCoordinates
        this.colorOne = colorOne
        this.colorTwo = colorTwo
    }

    override fun matches(piece : Piece) : Boolean
    {
        if(piece !is Edge) throw UnsupportedOperationException()
        else return (piece.colorOne == colorOne && piece.colorTwo == colorTwo)

    }

    override fun possessColor(color: Color): Boolean {
        return (colorOne == color || colorTwo == color)
    }

    override fun positionOfColor(color: Color): Int {
        return when (color) {
            colorOne -> 1
            colorTwo -> 2
            else -> 0
        }
    }
}