package app.service.cubeOLD

import app.helper.InitHelper
import app.model.cubeOLD.Cube
import app.model.cubeOLD.Side
import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import org.koin.core.KoinComponent
import org.koin.core.inject

class CubeService : KoinComponent {

    // TODO refactor ce service

    private val sideService : SideService by inject()
    val initHelper : InitHelper by inject()
    private lateinit var tempArray : Array<Color>

    fun isSolved(cube : Cube) : Boolean
    {
        var toReturn = true

        for(side in cube.sides.values)
        {
            if(!sideService.isSolved(side))
            {
                toReturn = false
            }
        }
        return toReturn
    }

    fun getSideByColor(cube : Cube, sideColor : Color) : Side
    {
        return cube.sides[sideColor]!!
    }

    fun getNumberOfEdgeOfAColorBySideColor(cube : Cube, sideColor : Color, edgeColor : Color) : Int
    {
        return sideService.getNumberOfEdgeOfAColor(getSideByColor(cube, sideColor), edgeColor)
    }

    fun getNumberOfCornerOfAColorBySideColor(cube : Cube, sideColor : Color, cornerColor : Color) : Int
    {
        return sideService.getNumberOfCornerOfAColor(getSideByColor(cube, sideColor), cornerColor)
    }

    fun getNumberOfSolved(cube : Cube) : Int
    {
        return cube.edges.filter{it.isSolved()}.size + cube.corners.filter { it.isSolved() }.size
    }

    private fun getLine(cube : Cube, sideColor: Color, index: Int) : Array<Color>
    {
        return sideService.getLine(getSideByColor(cube, sideColor), index)
    }

    private fun setLine(cube : Cube, sideColor : Color, index : Int, newElements : Array<Color>) : Array<Color>
    {
        return sideService.setLine(getSideByColor(cube, sideColor), index, newElements)
    }

    private fun getColumn(cube : Cube, sideColor: Color, index: Int) : Array<Color>
    {
        return sideService.getColumn(getSideByColor(cube, sideColor),index)
    }

    private fun setColumn(cube : Cube, sideColor : Color, index : Int, newElements : Array<Color>) : Array<Color>
    {
        return sideService.setColumn(getSideByColor(cube, sideColor), index, newElements)
    }

    private fun replaceLineOrColumn(cube : Cube, replaceType : String, reverse : Boolean, index : Int, sideColor : Color, newElements : Array<Color>) : Array<Color>
    {
        var toAdd : Array<Color>

        if(reverse)
        {
            toAdd = newElements.reversedArray()
        }
        else
        {
            toAdd = newElements
        }

        if(replaceType == "Line")
        {
            return setLine(cube, sideColor, index, toAdd)
        }
        else
        {
            return setColumn(cube, sideColor, index, toAdd)
        }
    }


    fun applySequence(cube : Cube, sequence : Array<Movement>)
    {
        for(movement in sequence)
        {
            applyMovement(cube, movement)
        }
    }

    fun applyMovement(cube : Cube, movement : Movement)
    {
        when(movement)
        {
            Movement.WHITE -> translationWhite(cube)
            Movement.ORANGE -> translationOrange(cube)
            Movement.GREEN -> translationGreen(cube)
            Movement.RED -> translationRed(cube)
            Movement.YELLOW -> translationYellow(cube)
            Movement.BLUE -> translationBlue(cube)
            Movement.WHITE_REVERSE -> translationWhitePrime(cube)
            Movement.ORANGE_REVERSE -> translationOrangePrime(cube)
            Movement.GREEN_REVERSE -> translationGreenPrime(cube)
            Movement.RED_REVERSE -> translationRedPrime(cube)
            Movement.YELLOW_REVERSE -> translationYellowPrime(cube)
            Movement.BLUE_REVERSE -> translationBluePrime(cube)
        }
    }


    private fun rotate(cube : Cube, color : Color, clockWise : Boolean)
    {
        var tempColor : Color

        if(clockWise) {
            // Corner permutations
            tempColor = sideService.setColor(getSideByColor(cube, color), 0, 2, sideService.getColor(getSideByColor(cube, color), 0, 0))
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 2, tempColor)
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 0, tempColor)
            sideService.setColor(getSideByColor(cube, color), 0, 0, tempColor)

            // Edge permutations
            tempColor = sideService.setColor(getSideByColor(cube, color), 0, 1, sideService.getColor(getSideByColor(cube, color), 1, 0))
            tempColor = sideService.setColor(getSideByColor(cube, color), 1, 2, tempColor)
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 1, tempColor)
            sideService.setColor(getSideByColor(cube, color), 1, 0, tempColor)
        }

        else {
            // Corner permutations
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 0, sideService.getColor(getSideByColor(cube, color), 0, 0))
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 2, tempColor)
            tempColor = sideService.setColor(getSideByColor(cube, color), 0, 2, tempColor)
            sideService.setColor(getSideByColor(cube, color), 0, 0, tempColor)

            // Edge permutations
            tempColor = sideService.setColor(getSideByColor(cube, color), 1, 0, sideService.getColor(getSideByColor(cube, color), 0, 1))
            tempColor = sideService.setColor(getSideByColor(cube, color), 2, 1, tempColor)
            tempColor = sideService.setColor(getSideByColor(cube, color), 1, 2, tempColor)
            sideService.setColor(getSideByColor(cube, color), 0, 1, tempColor)
        }
    }

    fun translationWhite(cube : Cube)
    {
        rotate(cube, Color.WHITE, true)
        tempArray = replaceLineOrColumn(cube, "Line", false,0, Color.ORANGE, getLine(cube, Color.GREEN, 0))
        tempArray = replaceLineOrColumn(cube, "Line", true,2, Color.BLUE, tempArray)
        tempArray = replaceLineOrColumn(cube, "Line", true,0, Color.RED, tempArray)
        replaceLineOrColumn(cube, "Line", false,0, Color.GREEN, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)

    }

    fun translationWhitePrime(cube : Cube)
    {
        rotate(cube, Color.WHITE, false)
        tempArray = replaceLineOrColumn(cube, "Line", false,0, Color.RED, getLine(cube, Color.GREEN, 0))
        tempArray = replaceLineOrColumn(cube, "Line", true,2, Color.BLUE, tempArray)
        tempArray = replaceLineOrColumn(cube, "Line", true,0, Color.ORANGE, tempArray)
        replaceLineOrColumn(cube, "Line", false,0, Color.GREEN, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationOrange(cube : Cube)
    {
        rotate(cube, Color.ORANGE, true)
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.GREEN, getColumn(cube, Color.WHITE, 0))
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.BLUE, tempArray)
        replaceLineOrColumn(cube, "Column", false,0, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationOrangePrime(cube : Cube)
    {
        rotate(cube, Color.ORANGE, false)
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.BLUE, getColumn(cube, Color.WHITE, 0))
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false,0, Color.GREEN, tempArray)
        replaceLineOrColumn(cube, "Column", false,0, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationGreen(cube : Cube)
    {
        rotate(cube, Color.GREEN, true)
        tempArray = replaceLineOrColumn(cube, "Column", false, 0, Color.RED, getLine(cube, Color.WHITE, 2))
        tempArray = replaceLineOrColumn(cube, "Line", true,0, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.ORANGE, tempArray)
        replaceLineOrColumn(cube, "Line", true,2, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationGreenPrime(cube : Cube)
    {
        rotate(cube, Color.GREEN, false)
        tempArray = replaceLineOrColumn(cube, "Column", true,2, Color.ORANGE, getLine(cube, Color.WHITE, 2))
        tempArray = replaceLineOrColumn(cube, "Line", false,0, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", true,0, Color.RED, tempArray)
        replaceLineOrColumn(cube, "Line", false,2, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationRed(cube : Cube)
    {
        rotate(cube, Color.RED, true)
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.BLUE, getColumn(cube, Color.WHITE, 2))
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.GREEN, tempArray)
        replaceLineOrColumn(cube, "Column", false,2, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationRedPrime(cube : Cube)
    {
        rotate(cube, Color.RED, false)
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.GREEN, getColumn(cube, Color.WHITE, 2))
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false,2, Color.BLUE, tempArray)
        replaceLineOrColumn(cube, "Column", false,2, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationYellow(cube : Cube)
    {
        rotate(cube, Color.YELLOW, true)
        tempArray = replaceLineOrColumn(cube, "Line", false,2, Color.RED, getLine(cube, Color.GREEN, 2))
        tempArray = replaceLineOrColumn(cube, "Line", true,0, Color.BLUE, tempArray)
        tempArray = replaceLineOrColumn(cube, "Line", true,2, Color.ORANGE, tempArray)
        replaceLineOrColumn(cube, "Line", false,2, Color.GREEN, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationYellowPrime(cube : Cube)
    {
        rotate(cube, Color.YELLOW, false)
        tempArray = replaceLineOrColumn(cube, "Line", false,2, Color.ORANGE, getLine(cube, Color.GREEN, 2))
        tempArray = replaceLineOrColumn(cube, "Line", true,0, Color.BLUE, tempArray)
        tempArray = replaceLineOrColumn(cube, "Line", true,2, Color.RED, tempArray)
        replaceLineOrColumn(cube, "Line", false,2, Color.GREEN, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationBlue(cube : Cube)
    {
        rotate(cube, Color.BLUE, true)
        tempArray = replaceLineOrColumn(cube, "Column", true,0, Color.ORANGE, getLine(cube, Color.WHITE, 0))
        tempArray = replaceLineOrColumn(cube, "Line", false,2, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", true,2, Color.RED, tempArray)
        replaceLineOrColumn(cube, "Line", false,0, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }

    fun translationBluePrime(cube : Cube)
    {
        rotate(cube, Color.BLUE, false)
        tempArray = replaceLineOrColumn(cube, "Column", false, 2, Color.RED, getLine(cube, Color.WHITE, 0))
        tempArray = replaceLineOrColumn(cube, "Line", true, 2, Color.YELLOW, tempArray)
        tempArray = replaceLineOrColumn(cube, "Column", false, 0, Color.ORANGE, tempArray)
        replaceLineOrColumn(cube, "Line", true, 0, Color.WHITE, tempArray)
        cube.corners = initHelper.initCorner(cube.sides)
        cube.edges = initHelper.initEdge(cube.sides)
    }


}