package app.model.cube.piece

import app.model.Color

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

    override fun getPositionOfColor(color: Color): Int? {
        if(colorOne == color) return 1
        else return null
    }

    override fun possessColor(color: Color): Boolean {
        return (colorOne==color)
    }

    override fun getColors(): Set<Color> {
        return setOf(colorOne)
    }

    override fun clone(): Piece {
        return Center(colorOne)
    }

}