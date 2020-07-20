package app.UI

import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement
import app.model.orientation.RelativePosition
import app.service.cube.CubeInformationService
import com.github.ajalt.mordant.TermColors
import org.koin.core.KoinComponent
import org.koin.core.inject

class ConsoleUI : KoinComponent {

    val cubeInformationService : CubeInformationService by inject()

    fun displayCube(cube : Cube)
    {
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.TOP),0)
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.TOP),1)
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.TOP),2)
        println()
        println()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.LEFT),0)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.FRONT),0)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.RIGHT),0)
        //print("[" + booleanToString(cube.corners[0].isSolved()) + " - " + booleanToString(cube.edges[0].isSolved()) + " - " + booleanToString(cube.corners[1].isSolved()) + "]")

        println()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.LEFT),1)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.FRONT),1)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.RIGHT),1)
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[1].isSolved()) + " -   - " + booleanToString(cube.edges[2].isSolved()) + "]")
        println()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.LEFT),2)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.FRONT),2)
        print("  ")
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.RIGHT),2)
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[2].isSolved()) + " - " + booleanToString(cube.edges[3].isSolved()) + " - " + booleanToString(cube.corners[3].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.BOTTOM),0)
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube,  RelativePosition.BOTTOM),1)
        println()
        displayEmptyLine()

        displayRow(cubeInformationService.getSideByRelativePosition(cube,  RelativePosition.BOTTOM),2)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[6].isSolved()) + " -   - " + booleanToString(cube.edges[7].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube, RelativePosition.BACK),0)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[4].isSolved()) + " - " + booleanToString(cube.edges[8].isSolved()) + " - " + booleanToString(cube.corners[5].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube,  RelativePosition.BACK),1)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.edges[9].isSolved()) + " -   - " + booleanToString(cube.edges[10].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeInformationService.getSideByRelativePosition(cube,  RelativePosition.BACK),2)
        displayEmptyLine()
        displayEmptyLine()
        //print("[" + booleanToString(cube.corners[6].isSolved()) + " - " + booleanToString(cube.edges[11].isSolved()) + " - " + booleanToString(cube.corners[7].isSolved()) + "]")
        println()
        println()
    }

    fun displaySequence(sequence : Array<Movement>)
    {
        print("Sequence lenght : " + sequence.size + " - ")
        for(elem in sequence)
        {
            print(elem)
            print(" ")
        }
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