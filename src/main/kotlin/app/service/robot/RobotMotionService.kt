package app.service.robot

import app.model.robot.Servo
import app.model.orientation.RelativePosition
import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoPositions
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashMap

/*
    Second layer of the driver
    Manages movements, and medium-level operations. (Turn a hand, etc.)
 */
class RobotMotionService : KoinComponent {

    var servos = HashMap<ServoPositions, Servo>()

    val servoControler : ServoService by inject()

    val movementHandSleep: Long = 600
    val movementArmSleep: Long = 400

    private val logger = LoggerFactory.getLogger(RobotMotionService::class.java)


    fun init()
    {
        servos = servoControler.initServos()
    }

    fun release()
    {
        executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
    }

    fun welcomePosition()
    {
        executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
        executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.NOT_TURNED, false)

        println("Appuyez sur entrée une fois le rubik's cube entré dans son emplacement")
        Scanner(System.`in`).nextLine()

        executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.NOT_TURNED, false)
        executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.INSIDE, true)
        executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.INSIDE, false)
        executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.INSIDE, false)
        executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.INSIDE, false)

        println("Appuyez sur entrée pour lancer la séquence")
        Scanner(System.`in`).nextLine()
    }

    fun turnCube(direction: RelativePosition)
    {
        if(direction == RelativePosition.RIGHT)
        {
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.NOT_TURNED, false)
            executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.INSIDE, true)
        }
        else if(direction == RelativePosition.LEFT)
        {
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_BOTTOM]!!, ServoState.NOT_TURNED, false)
            executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_TOP]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.INSIDE, true)
        }
        else if(direction == RelativePosition.TOP)
        {
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.TURNED, false)
            executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.INSIDE, true)
        }
        else if(direction == RelativePosition.BOTTOM)
        {
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.TURNED, true)
            executeCommand(servos[ServoPositions.ARM_RIGHT]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.OUTSIDE, false)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.TURNED, false)
            executeCommand(servos[ServoPositions.HAND_RIGHT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_BOTTOM]!!, ServoState.INSIDE, false)
            executeCommand(servos[ServoPositions.ARM_TOP]!!, ServoState.INSIDE, true)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.OUTSIDE, true)
            executeCommand(servos[ServoPositions.HAND_LEFT]!!, ServoState.NOT_TURNED, true)
            executeCommand(servos[ServoPositions.ARM_LEFT]!!, ServoState.INSIDE, true)
        }
    }

    fun turnHand(positionOnRobot : ServoPositions, clockwise : Boolean)
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

            if(positionOnRobot == ServoPositions.HAND_TOP) arm = servos[ServoPositions.ARM_TOP]!!
            if(positionOnRobot == ServoPositions.HAND_LEFT) arm = servos[ServoPositions.ARM_LEFT]!!
            if(positionOnRobot == ServoPositions.HAND_BOTTOM) arm = servos[ServoPositions.ARM_BOTTOM]!!
            if(positionOnRobot == ServoPositions.HAND_RIGHT) arm = servos[ServoPositions.ARM_RIGHT]!!

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
        if(wait)
        {
            if(servo.isAHand())
            {
                //logger.info("Waiting ! (hand)")
                Thread.sleep(movementHandSleep)
            }
            else if(servo.isAnArm())
            {
                //logger.info("Waiting ! (arm)")
                Thread.sleep(movementArmSleep)
            }
        }
    }

    fun isEveryHandHolding() : Boolean
    {
        return servos.all { it.value.getServoPosition() == ServoState.INSIDE || it.value.getServoPosition() == ServoState.NOT_TURNED }
    }
}