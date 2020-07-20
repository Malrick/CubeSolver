package app.service.robot

import app.model.robot.Servo
import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoPositions
import com.iamcontent.device.controller.pololu.maestro.DefaultPololuServoConfig
import com.iamcontent.device.servo.ServoSource
import com.iamcontent.device.servo.command.ImmutableServoCommand
import com.iamcontent.device.servo.command.ServoCommand
import com.iamcontent.device.servo.command.SimpleServoCommandExecutor
import org.slf4j.LoggerFactory


/*
    First layer of the driver
    Manages servos, and low-level operations. (Set a servo to a position, etc.)
 */
class ServoService {

    private var positionToChannel = HashMap<ServoPositions, Int>()
    private var servoSource: ServoSource<Int> = DefaultPololuServoConfig.normalServos()
    private var executor = SimpleServoCommandExecutor(servoSource)
    private val logger = LoggerFactory.getLogger(ServoService::class.java)

    fun initServos() : HashMap<ServoPositions, Servo>
    {
        positionToChannel[ServoPositions.HAND_TOP] = 3
        positionToChannel[ServoPositions.HAND_LEFT] = 0
        positionToChannel[ServoPositions.HAND_BOTTOM] = 1
        positionToChannel[ServoPositions.HAND_RIGHT] = 2

        positionToChannel[ServoPositions.ARM_TOP] = 9
        positionToChannel[ServoPositions.ARM_LEFT] = 6
        positionToChannel[ServoPositions.ARM_BOTTOM] = 7
        positionToChannel[ServoPositions.ARM_RIGHT] = 8

        var servos = HashMap<ServoPositions, Servo>()

        for(servoIdentity in ServoPositions.values()) {
            var toAdd = Servo()
            toAdd.setChannel(positionToChannel[servoIdentity]!!)
            toAdd.setPositionOnRobot(servoIdentity)
            toAdd.setServoPosition(getPositionOfServo(toAdd)!!)
            if (servoIdentity.name.startsWith("HAND"))
            {
                toAdd.setCalibrationValueNotTurned(0.0)
                toAdd.setCalibrationValueTurned(0.69)
            }
            else {
                toAdd.setCalibrationValueNotTurned(0.0)
                toAdd.setCalibrationValueTurned(0.8)
            }
            servos[servoIdentity] = toAdd
        }

        return servos
    }

    fun areAllServosInPosition(servosAndPositions : HashMap<Servo, ServoState>) : Boolean
    {
        for((servo, position) in servosAndPositions)
        {
            if(getPositionOfServo(servo) != position) return false
        }
        return true
    }

    fun getPositionOfServo(servo : Servo) : ServoState? {

        var channel = servo.getChannel()
        var positionOfServo = servoSource.forChannel(channel).value

        if(servo.isAnArm())
        {
            if(positionOfServo ==servo.getCalibrationValueTurned()) return ServoState.OUTSIDE
            else if(positionOfServo == servo.getCalibrationValueNotTurned()) return ServoState.INSIDE
            else return ServoState.CURRENTLY_MOVING
        }
        else if(servo.isAHand())
        {
            if(positionOfServo == servo.getCalibrationValueTurned()) return ServoState.TURNED
            else if(positionOfServo == servo.getCalibrationValueNotTurned()) return ServoState.NOT_TURNED
            else return ServoState.CURRENTLY_MOVING
        }
        else
        {
            println("get position of servo : unknown servo")
            return null
        }

    }

    fun moveServo(servo: Servo, positionOfServo: ServoState) {

        var command = getCommand(servo, positionOfServo)

        //logger.info("Mouvement du servo ${servo.getPositionOnRobot()} à la position $positionOfServo")
        executor.execute(command)
        //logger.info("Mouvement exécuté")

        servo.setServoPosition(positionOfServo)
    }


    private fun getCommand(servo : Servo, positionOfServo: ServoState) : ServoCommand<Int>
    {
        var newPosition : Double

        if(    servo.getPositionOnRobot().name.startsWith("ARM") && positionOfServo == ServoState.OUTSIDE
            || servo.getPositionOnRobot().name.startsWith("HAND") && positionOfServo == ServoState.TURNED)
        {
            newPosition = servo.getCalibrationValueTurned()
        }
        else
        {
            newPosition = servo.getCalibrationValueNotTurned()
        }

        return ImmutableServoCommand(servo.getChannel(), newPosition, 1.0, 1.0)
    }

}