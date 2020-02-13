package app.robotLink

import app.model.constants.Color
import app.model.constants.Movement
import app.model.robot.Orientation
import app.model.robot.Servo
import app.model.robot.constants.PositionOfCubeEnum
import app.model.robot.constants.PositionOnRobot
import org.koin.core.KoinComponent
import org.koin.core.inject

class MotionService : KoinComponent {

    private val servoService : ServoService by inject()

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
            servoService.turnCube()
            orientation.turnCube(PositionOfCubeEnum.RIGHT)
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