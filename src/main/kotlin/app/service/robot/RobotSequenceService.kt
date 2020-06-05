package app.service.robot

import app.model.Color
import app.model.movement.Movement
import app.model.orientation.Orientation
import app.model.movement.RelativePosition
import app.model.robot.constants.ServoIdentity
import app.model.robot.constants.ServoState
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.util.*

class RobotSequenceService : KoinComponent {

    private val robotMotionService : RobotMotionService by inject()
    private val videoManager : RobotVisionService by inject()

    private val orientation = Orientation()

    var countPic = 0

    fun init()
    {
        robotMotionService.init()
        orientation.init()
    }

    fun release()
    {
        robotMotionService.release()
    }

    fun welcome()
    {
        robotMotionService.welcomePosition()
    }

    fun turnCube(positionOfCubeEnum: RelativePosition)
    {
        robotMotionService.turnCube(positionOfCubeEnum)
    }

    fun closeCam()
    {
        videoManager.closeCam()
    }

    fun prepareCubeToPicture(color : Color)
    {
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
    }

    fun temp()
    {
        robotMotionService.turnCube(RelativePosition.TOP)
        robotMotionService.turnCube(RelativePosition.TOP)
    }

    fun takePicturesAndGetColors() : Array<Mat>
    {
        var finalColorsResolved = arrayOf<Mat>()
        var colorsDetectedOnFirstPicture = arrayOf<Mat>()
        var colorsDetectedOnSecondPicture = arrayOf<Mat>()

        // The arms at TOP and BOTTOM are getting outside, in order to be able to detect these colors
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)

        Thread.sleep(300)
        // Taking a picture, and getting an array of detected colors
        colorsDetectedOnFirstPicture += videoManager.getColorsFromPicture(true, "temp$countPic.jpg")

        countPic++

        // Arms at top and bottom are coming back to hold the cube, LEFT and RIGHT arms are going out
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)

        // Taking another picture
        Thread.sleep(300)
        colorsDetectedOnSecondPicture += videoManager.getColorsFromPicture(true, "temp$countPic.jpg")

        countPic++

        // Arms RIGHT and LEFT are coming back to hold the cube (initial position)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, false)
        robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)

        for (i in 0 until 9) {
            if (i == 3 || i == 5) {
                finalColorsResolved += colorsDetectedOnSecondPicture[i]
            } else if (i == 1 || i == 7) {
                finalColorsResolved += colorsDetectedOnFirstPicture[i]
            } else {
                finalColorsResolved += colorsDetectedOnFirstPicture[i]
            }
        }

        return finalColorsResolved
    }

    fun applySequence(sequence : Array<Movement>)
    {
        robotMotionService.welcomePosition()

        for(movement in sequence)
        {
            execute(movement)
        }

        robotMotionService.release()
    }

    fun execute(movement: Movement)
    {
        val colorOfMovement = Color.values().first { movement.name.startsWith(it.name) }
        var position = orientation.getPositionOfColor(colorOfMovement)
        val clockwise = !movement.name.endsWith("REVERSE")

        if(orientation.getPositionOfColor(colorOfMovement).equals(RelativePosition.BACK) || orientation.getPositionOfColor(colorOfMovement).equals(
                RelativePosition.FRONT))
        {
            val random = Random().nextInt(4)
            val rotationDirection : RelativePosition

            rotationDirection = when(random)
            {
                1 -> RelativePosition.RIGHT
                2 -> RelativePosition.LEFT
                3 -> RelativePosition.TOP
                else -> RelativePosition.BOTTOM
            }

            robotMotionService.turnCube(rotationDirection)
            orientation.turnCube(rotationDirection)
            position = orientation.getPositionOfColor(colorOfMovement)
        }

        when(position)
        {
            RelativePosition.TOP -> robotMotionService.turnHand(ServoIdentity.HAND_TOP, clockwise)
            RelativePosition.LEFT -> robotMotionService.turnHand(ServoIdentity.HAND_LEFT, clockwise)
            RelativePosition.BOTTOM -> robotMotionService.turnHand(ServoIdentity.HAND_BOTTOM, clockwise)
            RelativePosition.RIGHT -> robotMotionService.turnHand(ServoIdentity.HAND_RIGHT, clockwise)
        }
    }

}