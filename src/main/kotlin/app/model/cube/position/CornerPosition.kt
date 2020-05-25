package app.model.cube.position

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Corner
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class CornerPosition(override var cubeCoordinates: CubeCoordinates, var colorOne : Color, var colorTwo : Color, var colorThree : Color) : Position()
{
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