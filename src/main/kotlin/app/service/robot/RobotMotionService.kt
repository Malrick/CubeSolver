package app.service.robot

import app.model.robot.Servo
import app.model.cubeUtils.RelativePosition
import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoIdentity
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.HashMap

class RobotMotionService : KoinComponent {

    var servos = HashMap<ServoIdentity, Servo>()

    val servoControler : ServoService by inject()

    fun init()
    {
        servos = servoControler.initServos()
    }

    fun release()
    {
        executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
    }

    fun welcomePosition()
    {
        executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.NOT_TURNED, false)

        println("Appuyez sur entrée une fois le rubik's cube entré dans son emplacement")
        Scanner(System.`in`).nextLine()

        executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
        executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, false)
        executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, false)

        println("Appuyez sur entrée pour lancer la séquence")
        Scanner(System.`in`).nextLine()
    }

    fun turnCube(positionOfCubeEnum: RelativePosition)
    {
        // vers la droite

        if(positionOfCubeEnum == RelativePosition.RIGHT)
        {
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.NOT_TURNED, false)
            executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
        }
        else if(positionOfCubeEnum == RelativePosition.LEFT)
        {
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
            executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_TOP]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, true)
        }
        else if(positionOfCubeEnum == RelativePosition.TOP)
        {
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.TURNED, false)
            executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, true)
        }
        else if(positionOfCubeEnum == RelativePosition.BOTTOM)
        {
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_RIGHT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.TURNED, false)
            executeCommand(servos[ServoIdentity.HAND_RIGHT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_BOTTOM]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoIdentity.ARM_TOP]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoIdentity.HAND_LEFT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoIdentity.ARM_LEFT]!!, ServoState.INSIDE, true)
        }
    }

    fun turnHand(positionOnRobot : ServoIdentity, clockwise : Boolean)
    {
        if(!isEveryHandHolding())
        {
            println("Can't operate if hands are not holding")
        }
        else if(!positionOnRobot.name.startsWith("HAND", true))
        {
            println("Can't turn a arm like a hand")
        }
        else
        {
            var hand = servos[positionOnRobot]!!

            var arm = Servo()

            if(positionOnRobot == ServoIdentity.HAND_TOP) arm = servos[ServoIdentity.ARM_TOP]!!
            if(positionOnRobot == ServoIdentity.HAND_LEFT) arm = servos[ServoIdentity.ARM_LEFT]!!
            if(positionOnRobot == ServoIdentity.HAND_BOTTOM) arm = servos[ServoIdentity.ARM_BOTTOM]!!
            if(positionOnRobot == ServoIdentity.HAND_RIGHT) arm = servos[ServoIdentity.ARM_RIGHT]!!

            if(clockwise)
            {
                executeCommand(hand, ServoState.TURNED, true)
                executeCommand(arm, ServoState.OUTSIDE, true)
                executeCommand(hand, ServoState.NOT_TURNED, true)
                executeCommand(arm, ServoState.INSIDE, true)
            }
            else
            {
                executeCommand(arm, ServoState.OUTSIDE, true)
                executeCommand(hand, ServoState.TURNED, true)
                executeCommand(arm, ServoState.INSIDE, true)
                executeCommand(hand, ServoState.NOT_TURNED, true)
            }
        }

    }

    fun executeCommand(servo : Servo, positionOfServo: ServoState, wait : Boolean)
    {
        servoControler.moveServo(servo, positionOfServo)
        if(wait) Thread.sleep(350)
    }

    fun isEveryHandHolding() : Boolean
    {
        return servos.all { it.value.getServoPosition() == ServoState.INSIDE || it.value.getServoPosition() == ServoState.NOT_TURNED }
    }
}