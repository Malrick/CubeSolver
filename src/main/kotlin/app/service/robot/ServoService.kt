package app.service.robot

import app.model.robot.Servo
import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoIdentity
import com.iamcontent.device.controller.pololu.maestro.DefaultPololuServoConfig
import com.iamcontent.device.servo.ServoSource
import com.iamcontent.device.servo.command.ImmutableServoCommand
import com.iamcontent.device.servo.command.ServoCommand
import com.iamcontent.device.servo.command.SimpleServoCommandExecutor

class ServoService {

    private var positionToChannel = HashMap<ServoIdentity, Int>()
    private var servoSource: ServoSource<Int> = DefaultPololuServoConfig.normalServos()
    private var executor = SimpleServoCommandExecutor(servoSource)

    fun initServos() : HashMap<ServoIdentity, Servo>
    {
        positionToChannel[ServoIdentity.HAND_TOP] = 3
        positionToChannel[ServoIdentity.HAND_LEFT] = 0
        positionToChannel[ServoIdentity.HAND_BOTTOM] = 1
        positionToChannel[ServoIdentity.HAND_RIGHT] = 2

        positionToChannel[ServoIdentity.ARM_TOP] = 9
        positionToChannel[ServoIdentity.ARM_LEFT] = 6
        positionToChannel[ServoIdentity.ARM_BOTTOM] = 7
        positionToChannel[ServoIdentity.ARM_RIGHT] = 8

        var servos = HashMap<ServoIdentity, Servo>()

        for(servoIdentity in ServoIdentity.values()) {
            var toAdd = Servo()
            toAdd.setChannel(positionToChannel[servoIdentity]!!)
            toAdd.setPositionOnRobot(servoIdentity)
            toAdd.setServoPosition(getPositionOfServo(toAdd)!!)
            if (servoIdentity.name.startsWith("HAND"))
            {
                toAdd.setCalibrationValueNotTurned(0.0)
                toAdd.setCalibrationValueTurned(0.65)
            }
            else {
                toAdd.setCalibrationValueNotTurned(0.0)
            }
            servos[servoIdentity] = toAdd
        }

        return servos
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

        executor.execute(command)

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