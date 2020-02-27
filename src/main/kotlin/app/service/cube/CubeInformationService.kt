package app.service.cube

import app.model.cubeUtils.Color
import app.model.cube.Cube
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.cube.position.Position

class CubeInformationService {

    fun isSolved(cube : Cube) : Boolean
    {
        return cube.positions.all { it.key.matches(it.value) }
    }

    fun getNumberOfSolved(cube : Cube) : Int
    {
        return cube.positions.filter { it.key.matches(it.value) }.size
    }

    fun getSideByColor(cube : Cube, color : Color) : ArrayList<Color> {

        var toReturn = ArrayList<Color>()

        var sortedKeys = cube.positions.keys.filter { it.possessColor(color) }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube.cubeSize).coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube.cubeSize).coordY }


        for(elem in sortedKeys)
        {
            toReturn.add(cube.positions[elem]!!.getColorAtPosition(elem.positionOfColor(color))!!)
        }

        return toReturn
    }

    fun initSideByColor(cube : Cube, color : Color, colors : ArrayList<Color>)  {

        var sortedKeys = cube.positions.keys.filter { it.possessColor(color) }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube.cubeSize).coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube.cubeSize).coordY }

        var i = 0

        for(elem in sortedKeys)
        {
            cube.positions[elem]!!.setColorAtPosition(elem.positionOfColor(color), colors[i])
            i++
        }
    }

    fun getNumberOfCornersSolvedBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(color) && it.key is CornerPosition && it.key.matches(it.value)}.size
    }

    fun getNumberOfEdgesSolvedBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(color) && it.key is EdgePosition && it.key.matches(it.value)}.size
    }

    fun numberOfEdgesOfAColorSolved(cube : Cube, colorOne : Color, colorTwo : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(colorOne) && it.key.possessColor(colorTwo) && it.key is EdgePosition && it.key.matches(it.value)}.size
    }

    fun isCornerOfAColorSolved(cube : Cube, colorOne : Color, colorTwo : Color, colorThree : Color) : Boolean
    {
        return !cube.positions.none{ it.key.possessColor(colorOne) && it.key.possessColor(colorTwo) && it.key.possessColor(colorThree) && it.key.matches(it.value)}
    }

    fun getPositionsOfEdges(cube : Cube, colorOne : Color, colorTwo : Color) : Set<Position>
    {
        return cube.positions.filter { it.value.containsColor(colorOne) && it.value.containsColor(colorTwo) && it.key is EdgePosition }.keys
    }

}