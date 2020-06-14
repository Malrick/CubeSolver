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

class RobotOtvintaService : RobotService, KoinComponent {

    private val robotMotionService : RobotMotionService by inject()
    private val robotVisionService : RobotVisionService by inject()

    private lateinit var orientation : Orientation

    var countPic = 0

    override fun init(orientation : Orientation)
    {
        robotMotionService.init()
        this.orientation = orientation
    }

    override fun release()
    {
        robotMotionService.release()
    }

    override fun welcome()
    {
        robotMotionService.welcomePosition()
    }

    override fun turnCube(positionOfCubeEnum: RelativePosition)
    {
        robotMotionService.turnCube(positionOfCubeEnum)
    }

    private fun prepareCubeToPicture(position : RelativePosition)
    {
        when (position) {
            RelativePosition.TOP -> {
                robotMotionService.turnCube(RelativePosition.TOP)
            }
            RelativePosition.LEFT -> {
                robotMotionService.turnCube(RelativePosition.BOTTOM)
                robotMotionService.turnCube(RelativePosition.RIGHT)
            }
            RelativePosition.FRONT -> robotMotionService.turnCube(RelativePosition.LEFT)
            RelativePosition.RIGHT -> robotMotionService.turnCube(RelativePosition.LEFT)
            RelativePosition.BOTTOM -> {
                robotMotionService.turnCube(RelativePosition.RIGHT)
                robotMotionService.turnCube(RelativePosition.BOTTOM)
            }
            RelativePosition.BACK -> robotMotionService.turnCube(RelativePosition.BOTTOM)
        }
    }

    fun endColorDetection()
    {
        robotMotionService.turnCube(RelativePosition.RIGHT)
        robotMotionService.turnCube(RelativePosition.RIGHT)
        robotVisionService.closeCam()
    }

    override fun takePicturesAndGetColors() : HashMap<Color,Array<Mat>>
    {
        var detectedColors = HashMap<Color, Array<Mat>>()

        for(color in Color.values())
        {
            var finalColorsResolved = arrayOf<Mat>()

            prepareCubeToPicture(orientation.getPositionOfColor(color))

            // The arms at TOP and BOTTOM are getting outside, in order to be able to detect these colors
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)

            var colorsDetectedOnFirstPicture = robotVisionService.getColorsFromPicture(true, "img/waste.jpg")
            Thread.sleep(250)
            // Taking a picture, and getting an array of detected colors
            colorsDetectedOnFirstPicture = robotVisionService.getColorsFromPicture(true, "img/temp$countPic.jpg")

            countPic++

            // Arms at top and bottom are coming back to hold the cube, LEFT and RIGHT arms are going out
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
            robotMotionService.executeCommand(robotMotionService.servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)

            // Taking another picture
            Thread.sleep(250)
            var colorsDetectedOnSecondPicture = robotVisionService.getColorsFromPicture(true, "img/temp$countPic.jpg")

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

            detectedColors[color] = finalColorsResolved

        }

        endColorDetection()

        return detectedColors
    }

    override fun applySequence(sequence : Array<Movement>)
    {
        robotMotionService.welcomePosition()

        for(movement in sequence)
        {
            applyMovement(movement)
        }

        robotMotionService.release()
    }

    override fun applyMovement(movement: Movement)
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