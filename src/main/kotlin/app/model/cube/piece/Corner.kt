package app.model.cube.piece

import app.model.Color

class Corner(var colorOne : Color, var colorTwo : Color, var colorThree : Color) : Piece() {

    override fun getColorAtPosition(positionOfColor: Int) : Color? {
        return when (positionOfColor) {
            1 -> colorOne
            2 -> colorTwo
            3 -> colorThree
            else -> null
        }
    }

    override fun setColorAtPosition(positionOfColor: Int, color: Color) {
        when (positionOfColor) {
            1 -> colorOne = color
            2 -> colorTwo = color
            3 -> colorThree = color
        }
    }

    override fun getPositionOfColor(color: Color): Int? {
        return when(color)
        {
            colorOne -> 1
            colorTwo -> 2
            colorThree -> 3
            else -> null
        }
    }

    override fun possessColor(color: Color): Boolean {
        return (colorOne == color || colorTwo == color || colorThree == color)
    }

    override fun getColors(): Set<Color> {
        return setOf(colorOne, colorTwo, colorThree)
    }

    override fun clone(): Piece {
        return Corner(colorOne, colorTwo, colorThree)
    }


}