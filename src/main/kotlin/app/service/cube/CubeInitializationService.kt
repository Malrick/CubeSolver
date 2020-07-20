package app.service.cube

import app.UI.ConsoleUI
import app.UI.MatricesDisplay
import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement
import app.model.robot.vision.ColorProcessing
import app.service.robot.RobotOtvintaService
import app.service.robot.ColorResolver
import app.utils.files.CsvUtils
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.HashMap

class CubeInitializationService : KoinComponent {

    private val cubeInformationService: CubeInformationService by inject()
    private val robotService : RobotOtvintaService by inject()
    private val colorResolver : ColorResolver by inject()
    private val cubeMotionService : CubeMotionService by inject()
    private val matricesDisplay : MatricesDisplay by inject()
    private val csvUtils : CsvUtils by inject()

    private fun initCube(cube : Cube, colors : HashMap<Color, Array<Color>>)  {
        for((sideColor, colors) in colors)
        {
            var sortedKeys = cube.positions.keys.filter { it.possessColor(sideColor) }
                .sortedBy { it.cubeCoordinates.getSideCoordinate(sideColor, cube)!!.coordX }
                .sortedBy { it.cubeCoordinates.getSideCoordinate(sideColor, cube)!!.coordY }

            for(i in sortedKeys.indices)
            {
                cube.positions[sortedKeys[i]]!!.setColorAtPosition(sortedKeys[i].positionOfColor(sideColor), colors[i])
            }
        }
    }

    fun initSolvedCube(cube: Cube) {
        var colors = HashMap<Color, Array<Color>>()
        for (color in Color.values()) {
            colors[color] = Array(9) {color}
        }
        initCube(cube, colors)
    }

    fun initScrambledCube(cube : Cube, numberOfMoves : Int, displaySequence : Boolean)
    {
        var scramble = arrayOf<Movement>()

        initSolvedCube(cube)

        for(i in 0 until numberOfMoves)
        {
            scramble += Movement.values().random()
        }

        if(displaySequence)
        {
            ConsoleUI().displaySequence(scramble)
        }

        cubeMotionService.applySequence(cube, scramble)
    }


    fun initCubeByKeyboard(cube: Cube) {
        val input = Scanner(System.`in`)

        var colors = HashMap<Color, Array<Color>>()

        for (color in Color.values()) {
            var sideColors = arrayOf<Color>()

            println("Please enter the colors of the $color side")

            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    when (input.next()[0]) {
                        'W' -> sideColors += Color.WHITE
                        'O' -> sideColors += Color.ORANGE
                        'G' -> sideColors += Color.GREEN
                        'R' -> sideColors += Color.RED
                        'Y' -> sideColors += Color.YELLOW
                        'B' -> sideColors += Color.BLUE
                    }
                }
            }
            colors[color] = sideColors
        }
        initCube(cube, colors)
    }


    fun initCubeWithRobot(cube : Cube)
    {
        // Shows the pictures of the concatenated squares of colors
        val showPictures = false

        // Save color to csv (use to create Neural Networks, or do data analysis)
        val saveColors = false

        robotService.welcome()

        // Ask robot for colors
        var detectedColors = robotService.takePicturesAndGetColors()

        robotService.release()

        // Resolve colors, thanks to a certain method)
        var resolvedColors = colorResolver.resolve(detectedColors, ColorProcessing.ClosestDistance)

        initCube(cube, resolvedColors)

        if(showPictures) matricesDisplay.displayConcatenatedCube(detectedColors)

        if(saveColors) csvUtils.appendLabDataInCsvFile("colors", detectedColors)

        // If the cube is not valid, for now we throw an exception.
        if(!cubeInformationService.integrityCheck(cube))
        {
            throw Exception()
        }

    }


}