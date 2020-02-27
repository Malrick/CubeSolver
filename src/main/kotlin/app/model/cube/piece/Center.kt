package app.model.cube.piece

import app.model.cubeUtils.Color

class Center(var colorOne : Color) : Piece() {

    override fun getColorAtPosition(positionOfColor: Int) : Color? {
        return when (positionOfColor) {
            1 -> colorOne
            else -> null
        }
    }

    override fun setColorAtPosition(positionOfColor: Int, color: Color) {
        when (positionOfColor) {
            1 -> colorOne = color
        }
    }


    override fun containsColor(color: Color): Boolean {
        return (colorOne==color)
    }

    override fun clone(): Piece {
        return Center(colorOne)
    }

}