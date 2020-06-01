package app.model.cube.piece

import app.model.cubeUtils.Color

class Edge(var colorOne : Color, var colorTwo : Color) : Piece() {

    override fun getColorAtPosition(positionOfColor: Int) : Color? {
        return when (positionOfColor) {
            1 -> colorOne
            2 -> colorTwo
            else -> null
        }
    }

    override fun setColorAtPosition(positionOfColor: Int, color: Color) {
        when (positionOfColor) {
            1 -> colorOne = color
            2 -> colorTwo = color
        }
    }

    override fun containsColor(color: Color): Boolean {
        return (colorOne==color || colorTwo == color)
    }

    override fun getColors(): Set<Color> {
        return setOf(colorOne, colorTwo)
    }

    override fun clone(): Piece {
        return Edge(colorOne, colorTwo)
    }

}