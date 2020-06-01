package app.helper

import app.model.cube.Cube
import app.model.cubeUtils.Color
import app.model.cubeUtils.RelativePosition
import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoIdentity
import app.service.cube.CubeInformationService
import app.service.robot.RobotMotionService
import app.service.robot.RobotSequenceService
import app.vision.ColorResolver
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Scalar
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class InitHelper : KoinComponent {

    var colorResolver = ColorResolver()
    val cubeInformationService: CubeInformationService by inject()
    val robotMotionService: RobotMotionService by inject()
    val robotSequenceService : RobotSequenceService by inject()

    fun initSolvedCube(cube: Cube) {
        for (color in Color.values()) {
            var toAdd = ArrayList<Color>()
            for (i in 0 until 9) {
                toAdd.add(color)
            }
            cubeInformationService.initSideByColor(cube, color, toAdd)
        }
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

    fun initCubeWithRobot(cube: Cube) {

        var filePath = "colors/"

        var whiteLab = Scalar(0.0,0.0,0.0)

            for (color in Color.values()) {
            when (color) {
                Color.WHITE -> {
                    robotMotionService.turnCube(RelativePosition.TOP)
                }
                Color.ORANGE -> {
                    robotMotionService.turnCube(RelativePosition.BOTTOM)
                    robotMotionService.turnCube(RelativePosition.RIGHT)
                }
                Color.GREEN -> robotMotionService.turnCube(RelativePosition.LEFT)
                Color.RED -> robotMotionService.turnCube(RelativePosition.LEFT)
                Color.YELLOW -> {
                    robotMotionService.turnCube(RelativePosition.RIGHT)
                    robotMotionService.turnCube(RelativePosition.BOTTOM)
                }
                Color.BLUE -> robotMotionService.turnCube(RelativePosition.BOTTOM)
            }

            if(color == Color.WHITE)
            {
                var colorsDetectedOnFirstPicture = ArrayList<Scalar>()
                var colorsDetectedOnSecondPicture = ArrayList<Scalar>()
                // Taking a picture, and getting an array of detected colors
                colorsDetectedOnFirstPicture.addAll(colorResolver.processColorLabs())

                // Taking another picture
                colorsDetectedOnSecondPicture.addAll(colorResolver.processColorLabs())

                var averageWhiteL = (colorsDetectedOnFirstPicture[4].`val`[0] + colorsDetectedOnSecondPicture[4].`val`[0])/2
                var averageWhiteA = (colorsDetectedOnFirstPicture[4].`val`[1] + colorsDetectedOnSecondPicture[4].`val`[1])/2
                var averageWhiteB = (colorsDetectedOnFirstPicture[4].`val`[2] + colorsDetectedOnSecondPicture[4].`val`[2])/2

                whiteLab = Scalar(averageWhiteL, averageWhiteA, averageWhiteB)
            }

            var finalColorsResolved = ArrayList<Color>()
            var colorsDetectedOnFirstPicture = ArrayList<Color>()
            var colorsDetectedOnSecondPicture = ArrayList<Color>()

            // The arms at TOP and BOTTOM are getting outside, in order to be able to detect these colors
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)

            // Taking a picture, and getting an array of detected colors
            colorsDetectedOnFirstPicture.addAll(colorResolver.resolveColors(whiteLab,"temp.jpg", true, color))

            // Arms at top and bottom are coming back to hold the cube, LEFT and RIGHT arms are going out
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)

            // Taking another picture
            colorsDetectedOnSecondPicture.addAll(colorResolver.resolveColors(whiteLab, "temp2.jpg", true, color))

            // Arms RIGHT and LEFT are coming back to hold the cube (initial position)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)



            for (i in 0 until 9) {
                if (i == 3 || i == 5) {
                    finalColorsResolved.add(colorsDetectedOnSecondPicture[i])
                } else if (i == 1 || i == 7) {
                    finalColorsResolved.add(colorsDetectedOnFirstPicture[i])
                } else {
                    if (colorsDetectedOnFirstPicture[i] != colorsDetectedOnSecondPicture[i]) {
                        println("Two differents colors have been detected on two different pictures.")
                    }
                    // In case the two pictures have detected different colors, we choose the one of the first picture (arbitrary choice)
                    finalColorsResolved.add(colorsDetectedOnFirstPicture[i])
                }
            }

            if (finalColorsResolved[4] != color) {
                println("The color detected for the center is wrong.")
            }

            println("Final face $color :")
            for (elem in finalColorsResolved) {
                println(elem)
            }
            println()

            cubeInformationService.initSideByColor(cube, color, finalColorsResolved)
        }
    }

    fun processColorsAndSaveLab() {

        var finalColorsResolved = ArrayList<Scalar>()
        var whiteColorComparison = ArrayList<Scalar>()
        var colorsDetectedOnFirstPicture = ArrayList<Scalar>()
        var colorsDetectedOnSecondPicture = ArrayList<Scalar>()

        var averageWhiteL = 0.0
        var averageWhiteA = 0.0
        var averageWhiteB = 0.0

        var filePath = "colors/"

        for(color in Color.values()) {

            finalColorsResolved = ArrayList()
            colorsDetectedOnFirstPicture = ArrayList()
            colorsDetectedOnSecondPicture = ArrayList()

            when (color) {
                Color.WHITE -> {
                    robotMotionService.turnCube(RelativePosition.TOP)
                }
                Color.ORANGE -> {
                    robotMotionService.turnCube(RelativePosition.BOTTOM)
                    robotMotionService.turnCube(RelativePosition.RIGHT)
                }
                Color.GREEN -> robotMotionService.turnCube(RelativePosition.LEFT)
                Color.RED -> robotMotionService.turnCube(RelativePosition.LEFT)
                Color.YELLOW -> {
                    robotMotionService.turnCube(RelativePosition.RIGHT)
                    robotMotionService.turnCube(RelativePosition.BOTTOM)
                }
                Color.BLUE -> robotMotionService.turnCube(RelativePosition.BOTTOM)
            }

            //robotSequenceService.showSideToCamera(color)

            // The arms at TOP and BOTTOM are getting outside, in order to be able to detect these colors
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)

            // Taking a picture, and getting an array of detected colors
            //colorsDetectedOnFirstPicture.addAll(colorResolver.processColorLabs())

            // Arms at top and bottom are coming back to hold the cube, LEFT and RIGHT arms are going out
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)

            // Taking another picture
            //colorsDetectedOnSecondPicture.addAll(colorResolver.processColorLabs())

            // Arms RIGHT and LEFT are coming back to hold the cube (initial position)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)

            /*if(color == Color.WHITE)
            {
                averageWhiteL = (colorsDetectedOnFirstPicture[4].`val`[0] + colorsDetectedOnSecondPicture[4].`val`[0])/2
                averageWhiteA = (colorsDetectedOnFirstPicture[4].`val`[1] + colorsDetectedOnSecondPicture[4].`val`[1])/2
                averageWhiteB = (colorsDetectedOnFirstPicture[4].`val`[2] + colorsDetectedOnSecondPicture[4].`val`[2])/2

            }

            whiteColorComparison.add(Scalar(averageWhiteL, averageWhiteA, averageWhiteB))

            for (i in 0 until 9) {
                if (i == 3 || i == 5) {
                    finalColorsResolved.add(colorsDetectedOnSecondPicture[i])
                } else if (i == 1 || i == 7) {
                    finalColorsResolved.add(colorsDetectedOnFirstPicture[i])
                } else {
                    finalColorsResolved.add(colorsDetectedOnFirstPicture[i])
                    finalColorsResolved.add(colorsDetectedOnSecondPicture[i])
                }
            }
            filePath == "$color"*/
            //appendLabDataInCsvFile(filePath, color.name, whiteColorComparison, finalColorsResolved)
        }


        robotMotionService.turnCube(RelativePosition.TOP)
        robotMotionService.turnCube(RelativePosition.TOP)
    }

    fun appendLabDataInCsvFile(filePath : String, fileName : String, whiteData : List<Scalar>, labData : List<Scalar>)
    {
        var firstRow = listOf<Any>()
        var secondRow = listOf<Any>()
        var thirdRow = listOf<Any>()
        var fourthRow = listOf<Any>()
        var fifthRow = listOf<Any>()
        var sixthRow = listOf<Any>()

        firstRow = listOf()
        secondRow = listOf()
        thirdRow = listOf()
        fourthRow = listOf()
        fifthRow = listOf()
        sixthRow = listOf()

        var folder = File("$filePath")
        var file = File("$filePath$fileName.csv")

        folder.mkdirs()

        if(file.exists())
        {
            val oldRows: List<List<String>> = csvReader().readAll(file)

            firstRow+=oldRows[0]
            secondRow+=oldRows[1]
            thirdRow+=oldRows[2]
            fourthRow +=oldRows[3]
            fifthRow +=oldRows[4]
            sixthRow +=oldRows[5]
        }
        file.createNewFile()

        for(elem in labData)
        {
            firstRow+=whiteData[0].`val`[0].toInt()
            secondRow+=whiteData[0].`val`[1].toInt()
            thirdRow+=whiteData[0].`val`[2].toInt()
            fourthRow+=elem.`val`[0].toInt()
            fifthRow+=elem.`val`[1].toInt()
            sixthRow+=elem.`val`[2].toInt()
        }


        var rows = listOf(firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow)
        csvWriter().open(file) { writeAll(rows) }
    }


}
