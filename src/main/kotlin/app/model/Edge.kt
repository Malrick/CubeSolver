package app.model

import app.model.constants.Color

class Edge(name : Char, position : Char, solved: Boolean, colorOne : Color, colorTwo : Color) {

    var colorOne : Color = colorOne
    var colorTwo : Color = colorTwo

    private var position : Char = position
    private var name : Char = name
    private var solved : Boolean = solved

    fun getPosition() : Char
    {
        return position
    }

    fun isSolved() : Boolean
    {
        return solved
    }

    fun isBadlyOriented() : Boolean
    {
        return ((name == position) && !solved)
    }

    fun getName() : Char
    {
        return name
    }
}