package app.service

import app.model.Side
import app.model.constants.Color
import java.util.*

class SideService{

    fun isSolved(side : Side) : Boolean
    {
        var toReturn = true

        for(array in side.colors)
        {
            for(c in array)
            {
                if(c != side.sideColor)
                {
                    toReturn = false
                }
            }
        }
        return toReturn
    }

    fun getNumberOfEdgeOfAColor(side : Side, color : Color) : Int
    {
        var result = 0
        if(side.colors[0][1] == color) result +=1
        if(side.colors[1][0] == color) result +=1
        if(side.colors[1][2] == color) result +=1
        if(side.colors[2][1] == color) result +=1
        return result
    }

    fun getNumberOfCornerOfAColor(side : Side, color : Color) : Int
    {
        var result = 0
        if(side.colors[0][0] == color) result +=1
        if(side.colors[0][2] == color) result +=1
        if(side.colors[2][0] == color) result +=1
        if(side.colors[2][2] == color) result +=1
        return result
    }

    fun getLine(side : Side, index: Int) : Array<Color>
    {
        return side.colors[index]
    }

    fun getColumn(side : Side, index: Int) : Array<Color>
    {
        var tempArray : Array<Color> = arrayOf()
        tempArray += side.colors[0][index]
        tempArray += side.colors[1][index]
        tempArray += side.colors[2][index]
        return tempArray
    }

    fun getColor(side : Side, lineIndex : Int, columnIndex : Int) : Color
    {
        return side.colors[lineIndex][columnIndex]
    }

    fun setLine(side : Side, index : Int, newElements : Array<Color>) : Array<Color>
    {
        var tempArray : Array<Color> = side.colors[index]
        side.colors[index] = newElements
        return tempArray
    }

    fun setColumn(side : Side, index : Int, newElements : Array<Color>) : Array<Color>
    {
        var tempArray : Array<Color> = arrayOf()
        tempArray += side.colors[0][index]
        tempArray += side.colors[1][index]
        tempArray += side.colors[2][index]

        side.colors[0][index] = newElements[0]
        side.colors[1][index] = newElements[1]
        side.colors[2][index] = newElements[2]
        return tempArray
    }

    fun setColor(side : Side, lineIndex : Int, columnIndex : Int, color : Color) : Color
    {
        var deletedColor = side.colors[lineIndex][columnIndex]
        side.colors[lineIndex][columnIndex] = color
        return deletedColor
    }

}