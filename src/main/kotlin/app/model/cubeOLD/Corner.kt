package app.model.cubeOLD

import app.model.cubeUtils.Color
import app.model.cubeOLD.constants.CornerPosition

class Corner(name : CornerPosition, position : CornerPosition, solved: Boolean, colorOne : Color, colorTwo : Color, colorThree : Color) {

    var colorOne : Color = colorOne
    var colorTwo : Color = colorTwo
    var colorThree : Color = colorThree

    private var position : CornerPosition = position
    private var name : CornerPosition = name
    private var solved : Boolean = solved

    fun isSolved() : Boolean
    {
        return solved
    }

    fun isBadlyOriented() : Boolean
    {
        return ((name == position) && !solved)
    }

    fun getPosition() : CornerPosition
    {
        return position
    }

    fun getName() : CornerPosition
    {
        return name
    }
}