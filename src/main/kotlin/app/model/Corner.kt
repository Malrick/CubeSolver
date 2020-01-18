package app.model

import app.model.constants.Color

class Corner(name : Char, position : Char, solved: Boolean, colorOne : Color, colorTwo : Color, colorThree : Color) {

    var colorOne : Color = colorOne
    var colorTwo : Color = colorTwo
    var colorThree : Color = colorThree

    private var position : Char = position
    private var name : Char = name
    private var solved : Boolean = solved

    fun isSolved() : Boolean
    {
        return solved
    }

    fun isBadlyOriented() : Boolean
    {
        return ((name == position) && !solved)
    }

    fun getPosition() : Char
    {
        return position
    }

    fun getName() : Char
    {
        return name
    }
}