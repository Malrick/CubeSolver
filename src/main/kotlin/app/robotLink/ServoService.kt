package app.robotLink

import app.model.robot.Servo
import app.model.robot.constants.PositionOfServo
import app.model.robot.constants.PositionOnRobot
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.ArrayList

class ServoService : KoinComponent{

    var servos = ArrayList<Servo>()

    val servoControler : ServoControler by inject()

    fun init()
    {
        servos = servoControler.initServos()
    }

    fun release()
    {
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_TOP }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_LEFT }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_BOTTOM }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_RIGHT }, PositionOfServo.OUTSIDE, false)
    }

    fun welcomePosition()
    {
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_RIGHT }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_LEFT }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_TOP }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_BOTTOM }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_TOP }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_BOTTOM }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_LEFT }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_RIGHT }, PositionOfServo.NOT_TURNED, false)

        // TODO bras horizontaux

        println("Appuyez sur entrée une fois le rubik's cube entré dans son emplacement")
        Scanner(System.`in`).nextLine()



        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_BOTTOM }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_TOP }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_LEFT }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_RIGHT }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_BOTTOM }, PositionOfServo.INSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_TOP }, PositionOfServo.INSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_LEFT }, PositionOfServo.INSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_RIGHT }, PositionOfServo.INSIDE, false)


        println("Appuyez sur entrée pour lancer la séquence")
        Scanner(System.`in`).nextLine()


    }

    fun turnCube()
    {
        // vers la droite

        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_TOP }, PositionOfServo.OUTSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_TOP }, PositionOfServo.TURNED, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_TOP }, PositionOfServo.INSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_LEFT }, PositionOfServo.OUTSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_RIGHT }, PositionOfServo.OUTSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_TOP }, PositionOfServo.NOT_TURNED, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_BOTTOM }, PositionOfServo.TURNED, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_LEFT }, PositionOfServo.INSIDE, false)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_RIGHT }, PositionOfServo.INSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_BOTTOM }, PositionOfServo.OUTSIDE, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.HAND_BOTTOM }, PositionOfServo.NOT_TURNED, true)
        executeCommand(servos.first { it.getPositionOnRobot() == PositionOnRobot.ARM_BOTTOM }, PositionOfServo.INSIDE, true)


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
            var hand = servos.first{it.getPositionOnRobot().equals(positionOnRobot)}

            var arm = Servo()

            if(positionOnRobot == PositionOnRobot.HAND_TOP) arm = servos.first{it.getPositionOnRobot().equals(PositionOnRobot.ARM_TOP)}
            if(positionOnRobot == PositionOnRobot.HAND_LEFT) arm = servos.first{it.getPositionOnRobot().equals(PositionOnRobot.ARM_LEFT)}
            if(positionOnRobot == PositionOnRobot.HAND_BOTTOM) arm = servos.first{it.getPositionOnRobot().equals(PositionOnRobot.ARM_BOTTOM)}
            if(positionOnRobot == PositionOnRobot.HAND_RIGHT) arm = servos.first{it.getPositionOnRobot().equals(PositionOnRobot.ARM_RIGHT)}

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
        for(servo in servos)
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