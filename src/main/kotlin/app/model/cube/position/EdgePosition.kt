package app.model.cube.position

import app.model.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class EdgePosition(override var cubeCoordinates: CubeCoordinates, var colorOne : Color, var colorTwo : Color) : Position()
{
    override var identity = getColors().sorted().joinToString { it.name }

    override fun matches(piece : Piece) : Boolean
    {
        if(piece is Edge) return (piece.colorOne == colorOne && piece.colorTwo == colorTwo)
        else throw UnsupportedOperationException()
    }

    override fun possessColor(color: Color): Boolean {
        return (colorOne == color || colorTwo == color)
    }

    override fun getColors(): Set<Color> {
        return setOf(colorOne, colorTwo)
    }

    override fun positionOfColor(color: Color): Int {
        return when (color) {
            colorOne -> 1
            colorTwo -> 2
            else -> 0
        }
    }
}