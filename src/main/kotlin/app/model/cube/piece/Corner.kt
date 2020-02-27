package app.model.cube.piece

import app.model.cubeUtils.Color

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

    override fun containsColor(color: Color): Boolean {
        return (colorOne == color || colorTwo == color || colorThree == color)
    }

    override fun clone(): Piece {
        return Corner(colorOne, colorTwo, colorThree)
    }


}