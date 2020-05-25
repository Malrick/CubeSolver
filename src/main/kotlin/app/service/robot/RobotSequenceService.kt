package app.service.robot

import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.cubeUtils.Orientation
import app.model.cubeUtils.RelativePosition
import app.model.robot.constants.ServoIdentity
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class RobotSequenceService : KoinComponent {

    private val robotMotionService : RobotMotionService by inject()

    val orientation = Orientation()

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
        val position = orientation.getPositionOfColor(colorOfMovement)
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