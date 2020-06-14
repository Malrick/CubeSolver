package app.service.cube

import app.UI.MatricesDisplay
import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement
import app.model.robot.vision.ColorProcessing
import app.service.robot.RobotOtvintaService
import app.service.robot.RobotColorService
import app.utils.files.CsvUtils
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.util.*
import kotlin.collections.HashMap

class CubeInitializationService : KoinComponent {

    private val cubeInformationService: CubeInformationService by inject()
    private val robotSequenceService : RobotOtvintaService by inject()
    private val colorResolver : RobotColorService by inject()
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

    fun initScrambledCube(cube : Cube, numberOfMoves : Int){
        initSolvedCube(cube)
        for(i in 0 until numberOfMoves) { cubeMotionService.applyMovement(cube, Movement.values().random()) }
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
        val showPictures = true
        val saveColors = false

        var detectedColors = HashMap<Color, Array<Mat>>()

        detectedColors = robotSequenceService.takePicturesAndGetColors()

        var resolvedColors = colorResolver.resolve(detectedColors, ColorProcessing.ClosestDistance)

        initCube(cube, resolvedColors)

        if(showPictures) matricesDisplay.displayConcatenatedCube(detectedColors)

        if(saveColors) csvUtils.appendLabDataInCsvFile("colors", detectedColors)

        if(!cubeInformationService.integrityCheck(cube)) throw Exception()

    }


}