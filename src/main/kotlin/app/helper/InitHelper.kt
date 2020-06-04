package app.helper

import app.model.cube.Cube
import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.vision.ColorProcessing
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.robot.RobotSequenceService
import app.vision.ColorResolver
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Core.hconcat
import org.opencv.core.Core.vconcat
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InitHelper : KoinComponent {

    val cubeInformationService: CubeInformationService by inject()
    val robotSequenceService : RobotSequenceService by inject()
    val colorResolver : ColorResolver by inject()
    val cubeMotionService : CubeMotionService by inject()


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

    fun displayMats(colors : HashMap<Color, Array<Mat>>, myNumber : Int)
    {
        var concatenatedMats = listOf<Mat>()
        var superConcatenation = Mat()
        for(color in Color.values())
        {
            var mat = Mat()
            hconcat(colors[color]?.asList(), mat)
            concatenatedMats += mat
        }
        vconcat(concatenatedMats, superConcatenation)
        HighGui.imshow("couleurs concaténées", superConcatenation)

        if(myNumber!=0) {
            concatenatedMats = listOf<Mat>()
            var dominantColors = HashMap<Color, MutableList<Mat>>()
            superConcatenation = Mat()
            var colorResolver = ColorResolver()

            for ((sideColor, colors) in colors) {
                dominantColors[sideColor] = mutableListOf()
                for (elem in colors) {
                    dominantColors[sideColor]?.plusAssign(elem.clone().setTo(colorResolver.KnnClustering(elem, myNumber)))
                }
            }

            for (color in Color.values()) {
                var mat = Mat()

                hconcat(dominantColors[color], mat)
                concatenatedMats += mat
            }
            vconcat(concatenatedMats, superConcatenation)
            HighGui.imshow("couleurs dominantes", superConcatenation)
        }

        HighGui.waitKey(0)
    }

    fun initCubeWithRobot(cube : Cube)
    {
        val showPictures = true

        var detectedColors = HashMap<Color, Array<Mat>>()

        for(color in Color.values())
        {
            robotSequenceService.prepareCubeToPicture(color)

            detectedColors[color] = robotSequenceService.takePicturesAndGetColors()
        }

        robotSequenceService.temp()

        if(showPictures) displayMats(detectedColors, 30)

        var resolvedColors = colorResolver.resolve(detectedColors, ColorProcessing.ClosestDistance)

        for((sideColor, colors) in resolvedColors)
        {
            cubeInformationService.initSideByColor(cube, sideColor, ArrayList(colors.toMutableList()))
        }

        robotSequenceService.closeCam()
    }

}
