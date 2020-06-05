package app.service.cube

import app.UI.MatricesDisplay
import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement
import app.model.vision.ColorProcessing
import app.service.robot.RobotSequenceService
import app.service.robot.ColorService
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CubeInitializationService : KoinComponent {

    val cubeInformationService: CubeInformationService by inject()
    val robotSequenceService : RobotSequenceService by inject()
    val colorResolver : ColorService by inject()
    val cubeMotionService : CubeMotionService by inject()
    val matricesDisplay : MatricesDisplay by inject()


    fun initSolvedCube(cube: Cube) {
        for (color in Color.values()) {
            var toAdd = ArrayList<Color>()
            for (i in 0 until 9) {
                toAdd.add(color)
            }
            cubeInformationService.initSideByColor(cube, color, toAdd)
        }
    }

    fun initScrambledCube(cube : Cube){
        initSolvedCube(cube)
        var randomMovements = listOf<Movement>()
        for(i in 0..50)
        {
            randomMovements += Movement.values().random()
        }
        cubeMotionService.applySequence(cube, randomMovements.toTypedArray())
    }


    fun initCubeByKeyboard(cube: Cube) {
        val input = Scanner(System.`in`)

        for (color in Color.values()) {
            var sideColors = ArrayList<Color>()

            println("Please enter the colors of the $color side")

            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    when (input.next()[0]) {
                        'W' -> sideColors.add(Color.WHITE)
                        'O' -> sideColors.add(Color.ORANGE)
                        'G' -> sideColors.add(Color.GREEN)
                        'R' -> sideColors.add(Color.RED)
                        'Y' -> sideColors.add(Color.YELLOW)
                        'B' -> sideColors.add(Color.BLUE)
                    }
                }
            }

            cubeInformationService.initSideByColor(cube, color, sideColors)
        }
    }

    fun initCubeWithRobot(cube : Cube)
    {
        val showPictures = false

        var detectedColors = HashMap<Color, Array<Mat>>()

        for(color in Color.values())
        {
            robotSequenceService.prepareCubeToPicture(color)

            detectedColors[color] = robotSequenceService.takePicturesAndGetColors()
        }

        robotSequenceService.temp()

        if(showPictures) matricesDisplay.displayConcatenatedCube(detectedColors)

        var resolvedColors = colorResolver.resolve(detectedColors, ColorProcessing.ClosestDistance)

        for((sideColor, colors) in resolvedColors)
        {
            cubeInformationService.initSideByColor(cube, sideColor, ArrayList(colors.toMutableList()))
        }

        robotSequenceService.closeCam()
    }


}