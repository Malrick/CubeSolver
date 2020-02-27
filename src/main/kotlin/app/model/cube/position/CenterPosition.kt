package app.model.cube.position

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Center
import app.model.cube.piece.Piece
import java.lang.UnsupportedOperationException

class CenterPosition : Position
{
    lateinit var colorOne : Color

    constructor(cubeCoordinates: CubeCoordinates, color : Color)
    {
        this.cubeCoordinates = cubeCoordinates
        this.colorOne = color
    }

    override fun matches(piece : Piece) : Boolean
    {
        if(piece !is Center) throw UnsupportedOperationException()
        else return piece.colorOne == colorOne
    }

    override fun possessColor(color: Color) : Boolean{
        return this.colorOne==color
    }

    override fun positionOfColor(color: Color): Int {
        return if(color == colorOne) 1
        else 0
    }
}