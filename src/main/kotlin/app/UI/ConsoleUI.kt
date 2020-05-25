package app.UI

import app.model.cube.Cube
import app.model.cubeUtils.Color
import app.service.cube.CubeInformationService
import com.github.ajalt.mordant.TermColors
import org.koin.core.KoinComponent
import org.koin.core.inject

class ConsoleUI : KoinComponent {

    val cubeInformationService : CubeInformationService by inject()

    fun displayCube(cube : Cube)
    {
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.WHITE),0)
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.WHITE),1)
        displayEmptyLine()
        displayEmptyLine()
        print("Solved (" + TermColors().red(cubeInformationService.getNumberOfSolved(cube).toString()) +")")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.WHITE),2)
        println()
        println()
        displayRow(cubeInformationService.getSideByColor(cube, Color.ORANGE),0)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.GREEN),0)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.RED),0)
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[0].isSolved()) + " - " + booleanToString(cube.edges[0].isSolved()) + " - " + booleanToString(cube.corners[1].isSolved()) + "]")

        println()
        displayRow(cubeInformationService.getSideByColor(cube, Color.ORANGE),1)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.GREEN),1)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.RED),1)
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[1].isSolved()) + " -   - " + booleanToString(cube.edges[2].isSolved()) + "]")
        println()
        displayRow(cubeInformationService.getSideByColor(cube, Color.ORANGE),2)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.GREEN),2)
        print("  ")
        displayRow(cubeInformationService.getSideByColor(cube, Color.RED),2)
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[2].isSolved()) + " - " + booleanToString(cube.edges[3].isSolved()) + " - " + booleanToString(cube.corners[3].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.YELLOW),0)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[4].isSolved()) + " -   - " + booleanToString(cube.edges[5].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.YELLOW),1)
        displayEmptyLine()
        displayEmptyLine()

        print("[ " + " -   - " + " ]")
        print("      ")
        print("[ " + " -   - " + " ]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.YELLOW),2)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[6].isSolved()) + " -   - " + booleanToString(cube.edges[7].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.BLUE),0)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[4].isSolved()) + " - " + booleanToString(cube.edges[8].isSolved()) + " - " + booleanToString(cube.corners[5].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.BLUE),1)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[9].isSolved()) + " -   - " + booleanToString(cube.edges[10].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByColor(cube, Color.BLUE),2)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[6].isSolved()) + " - " + booleanToString(cube.edges[11].isSolved()) + " - " + booleanToString(cube.corners[7].isSolved()) + "]")
        println()
    }

    private fun booleanToString(boolean : Boolean) : String
    {
        if(boolean) return TermColors().red("1")
        else return "0"
    }

    private fun displayRow(colors : List<Color>, rowNumber : Int)
    {
        var colorsToDisplay = listOf<Color>()

        when(rowNumber)
        {
            0 -> colorsToDisplay = colors.subList(0, 3)
            1 -> colorsToDisplay = colors.subList(3, 6)
            2 -> colorsToDisplay = colors.subList(6, 9)
        }

        print(colorToDisplayedString(colorsToDisplay[0]) + colorToDisplayedString(colorsToDisplay[0]) + colorToDisplayedString(colorsToDisplay[0]))
        print(colorToDisplayedString(colorsToDisplay[1]) + colorToDisplayedString(colorsToDisplay[1]) + colorToDisplayedString(colorsToDisplay[1]))
        print(colorToDisplayedString(colorsToDisplay[2]) + colorToDisplayedString(colorsToDisplay[2]) + colorToDisplayedString(colorsToDisplay[2]))
    }


    private fun colorToDisplayedString(color : Color) : String
    {
        when(color)
        {
            Color.BLUE -> with(TermColors()){ return((hsv(200,100,72) on hsv(200, 100, 72))("B"))}
            Color.GREEN -> with(TermColors()){ return((hsv(196,100,72) on hsv(196,100,72))("G"))}
            Color.RED -> with(TermColors()){ return((hsv(0,100,72) on hsv(0,100, 72))("R"))}
            Color.ORANGE -> with(TermColors()){ return((hsv(284,100,91) on hsv(284,100,92))("O"))}
            Color.WHITE -> with(TermColors()){ return((hsv(20,100,20) on hsv(20, 100, 20))("W"))}
            Color.YELLOW -> with(TermColors()){ return((hsv(32,100,100) on hsv(32,100,100))("Y"))}
        }
    }

    private fun displayEmptyLine()
    {
        for(i in 0..10)
        {
            print(" ")
        }
    }

}