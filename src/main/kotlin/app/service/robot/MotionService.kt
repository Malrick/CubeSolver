package app.service.robot

import app.model.robot.Servo
import app.model.robot.constants.PositionOfCubeEnum
import app.model.robot.constants.PositionOfServo
import app.model.robot.constants.PositionOnRobot
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.HashMap

class MotionService : KoinComponent{

    var servos = HashMap<PositionOnRobot, Servo>()

    val servoControler : ServoService by inject()

    fun init()
    {
        servos = servoControler.initServos()
    }

    fun release()
    {
        executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, false)
    }

    fun welcomePosition()
    {
        executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, false)
        executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.NOT_TURNED, false)

        // TODO bras horizontaux

        println("Appuyez sur entrée une fois le rubik's cube entré dans son emplacement")
        Scanner(System.`in`).nextLine()



        executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, true)
        executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, false)
        executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, false)


        println("Appuyez sur entrée pour lancer la séquence")
        Scanner(System.`in`).nextLine()


    }

    fun turnCube(positionOfCubeEnum: PositionOfCubeEnum)
    {
        // vers la droite

        if(positionOfCubeEnum == PositionOfCubeEnum.RIGHT)
        {
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.NOT_TURNED, false)
            executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, true)
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.LEFT)
        {
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_BOTTOM]!!, PositionOfServo.NOT_TURNED, false)
            executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_TOP]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, true)
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.TOP)
        {
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.TURNED, false)
            executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, true)
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.BOTTOM)
        {
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.TURNED, false)
            executeCommand(servos[PositionOnRobot.HAND_RIGHT]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, false)
            executeCommand(servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, true)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, true)
            executeCommand(servos[PositionOnRobot.HAND_LEFT]!!, PositionOfServo.NOT_TURNED, true)
            executeCommand(servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, true)
        }
    }

    fun turnHand(positionOnRobot : PositionOnRobot, clockwise : Boolean)
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

            if(positionOnRobot == PositionOnRobot.HAND_TOP) arm = servos[PositionOnRobot.ARM_TOP]!!
            if(positionOnRobot == PositionOnRobot.HAND_LEFT) arm = servos[PositionOnRobot.ARM_LEFT]!!
            if(positionOnRobot == PositionOnRobot.HAND_BOTTOM) arm = servos[PositionOnRobot.ARM_BOTTOM]!!
            if(positionOnRobot == PositionOnRobot.HAND_RIGHT) arm = servos[PositionOnRobot.ARM_RIGHT]!!

            if(clockwise)
            {
                executeCommand(hand, PositionOfServo.TURNED, true)
                executeCommand(arm, PositionOfServo.OUTSIDE, true)
                executeCommand(hand, PositionOfServo.NOT_TURNED, true)
                executeCommand(arm, PositionOfServo.INSIDE, true)
            }
            else
            {
                executeCommand(arm, PositionOfServo.OUTSIDE, true)
                executeCommand(hand, PositionOfServo.TURNED, true)
                executeCommand(arm, PositionOfServo.INSIDE, true)
                executeCommand(hand, PositionOfServo.NOT_TURNED, true)
            }
        }

    }

    fun executeCommand(servo : Servo, positionOfServo: PositionOfServo, wait : Boolean)
    {
        servoControler.moveServo(servo, positionOfServo)
        if(wait) Thread.sleep(350)
    }

    fun isEveryHandHolding() : Boolean
    {
        for(servo in servos.values)
        {
            var servoPosition = servo.getServoPosition()
            if(servoPosition != PositionOfServo.INSIDE && servoPosition != PositionOfServo.NOT_TURNED)
            {
                return false
            }
        }
        return true
    }
}