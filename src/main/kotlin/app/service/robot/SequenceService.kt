package app.service.robot

import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.robot.Orientation
import app.model.robot.constants.PositionOfCubeEnum
import app.model.robot.constants.PositionOnRobot
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class SequenceService : KoinComponent {

    private val servoService : MotionService by inject()

    val orientation = Orientation()

    fun init()
    {
        servoService.init()
        orientation.init()
    }

    fun release()
    {
        servoService.release()
    }

    fun welcome()
    {
        servoService.welcomePosition()
    }

    fun turnCube(positionOfCubeEnum: PositionOfCubeEnum)
    {
        servoService.turnCube(positionOfCubeEnum)
    }

    fun applySequence(sequence : Array<Movement>)
    {
        servoService.welcomePosition()

        for(movement in sequence)
        {
            execute(movement)
        }

        servoService.release()
    }

    fun execute(movement: Movement)
    {
        var colorOfMovement = Color.BLUE
        for(color in Color.values())
        {
            if(movement.name.startsWith(color.name, true))
            {
                colorOfMovement = color
            }
        }

        if(orientation.getPositionOfColor(colorOfMovement).equals(PositionOfCubeEnum.BACK) || orientation.getPositionOfColor(colorOfMovement).equals(PositionOfCubeEnum.FRONT))
        {
            var random = Random().nextInt(4)
            var rotationDirection : PositionOfCubeEnum

            when(random)
            {
                1 -> rotationDirection = PositionOfCubeEnum.RIGHT
                2 -> rotationDirection = PositionOfCubeEnum.LEFT
                3 -> rotationDirection = PositionOfCubeEnum.TOP
                else -> rotationDirection = PositionOfCubeEnum.BOTTOM
            }

            servoService.turnCube(rotationDirection)
            orientation.turnCube(rotationDirection)
        }

        var position = orientation.getPositionOfColor(colorOfMovement)
        var clockwise = !movement.name.endsWith("REVERSE", true)

        var hand = PositionOnRobot.HAND_TOP

        if(position == PositionOfCubeEnum.TOP) hand = PositionOnRobot.HAND_TOP
        if(position == PositionOfCubeEnum.LEFT) hand = PositionOnRobot.HAND_LEFT
        if(position == PositionOfCubeEnum.BOTTOM) hand = PositionOnRobot.HAND_BOTTOM
        if(position == PositionOfCubeEnum.RIGHT) hand = PositionOnRobot.HAND_RIGHT

        servoService.turnHand(hand, clockwise)
    }

}