package app.helper

import app.service.CubeService
import app.model.Cube
import app.model.Side
import app.model.constants.Color
import com.github.ajalt.mordant.TermColors
import org.koin.core.KoinComponent
import org.koin.core.inject

class DisplayHelper : KoinComponent {

    val cubeService : CubeService by inject()

    fun display(cube : Cube)
    {
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.WHITE),0)
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.WHITE),1)
        displayEmptyLine()
        displayEmptyLine()
        print("Pieces")
        displayEmptyLine()
        print("Solved")
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.WHITE),2)
        println()
        println()
        displayRow(cubeService.getSideByColor(cube, Color.ORANGE),0)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.GREEN),0)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.RED),0)
        displayEmptyLine()
        print("[" + cube.corners[0].getName() + " - " + cube.edges[0].getName() + " - " + cube.corners[1].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.corners[0].isSolved()) + " - " + booleanToInt(cube.edges[0].isSolved()) + " - " + booleanToInt(cube.corners[1].isSolved()) + "]")

        println()
        displayRow(cubeService.getSideByColor(cube, Color.ORANGE),1)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.GREEN),1)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.RED),1)
        displayEmptyLine()
        print("[" + cube.edges[1].getName() + " -   - " + cube.edges[2].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.edges[1].isSolved()) + " -   - " + booleanToInt(cube.edges[2].isSolved()) + "]")
        println()
        displayRow(cubeService.getSideByColor(cube, Color.ORANGE),2)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.GREEN),2)
        print("  ")
        displayRow(cubeService.getSideByColor(cube, Color.RED),2)
        displayEmptyLine()
        print("[" + cube.corners[2].getName() + " - " + cube.edges[3].getName() + " - " + cube.corners[3].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.corners[2].isSolved()) + " - " + booleanToInt(cube.edges[3].isSolved()) + " - " + booleanToInt(cube.corners[3].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.YELLOW),0)
        displayEmptyLine()
        displayEmptyLine()
        print("[" + cube.edges[4].getName() + " -   - " + cube.edges[5].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.edges[4].isSolved()) + " -   - " + booleanToInt(cube.edges[5].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.YELLOW),1)
        displayEmptyLine()
        displayEmptyLine()

        print("[ " + " -   - " + " ]")
        print("      ")
        print("[ " + " -   - " + " ]")
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.YELLOW),2)
        displayEmptyLine()
        displayEmptyLine()
        print("[" + cube.edges[6].getName() + " -   - " + cube.edges[7].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.edges[6].isSolved()) + " -   - " + booleanToInt(cube.edges[7].isSolved()) + "]")
        println()
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.BLUE),0)
        displayEmptyLine()
        displayEmptyLine()
        print("[" + cube.corners[4].getName() + " - " + cube.edges[8].getName() + " - " + cube.corners[5].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.corners[4].isSolved()) + " - " + booleanToInt(cube.edges[8].isSolved()) + " - " + booleanToInt(cube.corners[5].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.BLUE),1)
        displayEmptyLine()
        displayEmptyLine()
        print("[" + cube.edges[9].getName() + " -   - " + cube.edges[10].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.edges[9].isSolved()) + " -   - " + booleanToInt(cube.edges[10].isSolved()) + "]")
        println()
        displayEmptyLine()
        displayRow(cubeService.getSideByColor(cube, Color.BLUE),2)
        displayEmptyLine()
        displayEmptyLine()
        print("[" + cube.corners[6].getName() + " - " + cube.edges[11].getName() + " - " + cube.corners[7].getName() + "]")
        print("      ")
        print("[" + booleanToInt(cube.corners[6].isSolved()) + " - " + booleanToInt(cube.edges[11].isSolved()) + " - " + booleanToInt(cube.corners[7].isSolved()) + "]")
        println()
    }

    fun booleanToInt(boolean : Boolean) : Int
    {
        if(boolean) return 1
        else return 0
    }

    fun displayRow(side : Side, rowNumber : Int)
    {
        print(solveToDisplay(side.colors[rowNumber][0]) + solveToDisplay(side.colors[rowNumber][0])+ solveToDisplay(side.colors[rowNumber][0]) + solveToDisplay(side.colors[rowNumber][1])+ solveToDisplay(side.colors[rowNumber][1])+ solveToDisplay(side.colors[rowNumber][1]) + solveToDisplay(side.colors[rowNumber][2])+ solveToDisplay(side.colors[rowNumber][2]) +solveToDisplay(side.colors[rowNumber][2]))
    }

    fun solveToDisplay(color : Color) : String
    {
        /*
        when(color)
        {
            Color.BLUE -> return colorDisplay.blue("B")
            Color.GREEN -> return colorDisplay.green("B")
            Color.RED -> return colorDisplay.red("B")
            Color.ORANGE -> return colorDisplay.brightCyan("O")
            Color.WHITE -> return colorDisplay.brightWhite("W")
            Color.YELLOW -> return colorDisplay.brightYellow("Y")
        }

         */



        when(color)
        {
            Color.BLUE -> with(TermColors()){ return((hsv(200,100,72) on hsv(200, 100, 72))("B"))}
            Color.GREEN -> with(TermColors()){ return((hsv(196,100,72) on hsv(196,100,72))("G"))}
            Color.RED -> with(TermColors()){ return((hsv(0,100,72) on hsv(0,100, 72))("R"))}
            Color.ORANGE -> with(TermColors()){ return((hsv(284,100,91) on hsv(284,100,92))("O"))}
            Color.WHITE -> with(TermColors()){ return((hsv(20,100,20) on hsv(20, 100, 20))("W"))}
            Color.YELLOW -> with(TermColors()){ return((hsv(32,100,100) on hsv(32,100,100))("Y"))}
        }

        return "null"

    }


    fun displayEmptyLine()
    {
        for(i in 0..10)
        {
            print(" ")
        }
    }

    fun project(corners : Array<Char>, edges : Array<Char>)
    {
        print("[" + corners[0] + " - " + edges[0] + " - " + corners[1] + "]")
        println()
        print("[" + edges[1] +" -   - " + edges[2] + "]")
        println()
        print("[" + corners[2] + " - " + edges[3] + " - " + corners[3] + "]")
        println()
        println()
        print("["  + edges[4] + " -   - "  + edges[5] + "]")
        println()
        print("[  -   -  ]")
        println()
        print("["  + edges[6] + " -   - "  + edges[7] + "]")
        println()
        println()
        print("[" + corners[4] + " - " + edges[8] + " - " + corners[5] + "]")
        println()
        print("[" + edges[9] +" -   - " + edges[10] + "]")
        println()
        print("[" + corners[6] + " - " + edges[11] + " - " + corners[7] + "]")
        println()
    }


}