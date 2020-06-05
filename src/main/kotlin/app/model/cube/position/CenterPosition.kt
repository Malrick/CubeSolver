package app.model.cube.position

import app.model.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Center
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class CenterPosition(override var cubeCoordinates: CubeCoordinates, var colorOne : Color) : Position()
{
    override fun matches(piece : Piece) : Boolean
    {
        if(piece !is Center) throw UnsupportedOperationException()
        else return piece.colorOne == colorOne
    }

    override fun getColors(): Set<Color> {
        return setOf(colorOne)
    }

    override fun possessColor(color: Color) : Boolean{
        return this.colorOne==color
    }

    override fun positionOfColor(color: Color): Int {
        return if(color == colorOne) 1
        else 0
    }
}