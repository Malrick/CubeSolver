package app.robotLink

import app.model.robot.Servo
import app.model.robot.constants.PositionOfServo
import app.model.robot.constants.PositionOnRobot
import com.iamcontent.device.controller.pololu.maestro.DefaultPololuServoConfig
import com.iamcontent.device.servo.command.ImmutableServoCommand
import com.iamcontent.device.servo.command.ServoCommand
import com.iamcontent.device.servo.command.SimpleServoCommandExecutor

class ServoControler : ServoControl {

    var positionToChannel = HashMap<PositionOnRobot, Int>()

    var servoSource = DefaultPololuServoConfig.normalServos()

    var executor = SimpleServoCommandExecutor(servoSource)

    fun initServos() : ArrayList<Servo>
    {

        positionToChannel[PositionOnRobot.HAND_TOP] = 3
        positionToChannel[PositionOnRobot.HAND_LEFT] = 0
        positionToChannel[PositionOnRobot.HAND_BOTTOM] = 1
        positionToChannel[PositionOnRobot.HAND_RIGHT] = 2

        positionToChannel[PositionOnRobot.ARM_TOP] = 9
        positionToChannel[PositionOnRobot.ARM_LEFT] = 6
        positionToChannel[PositionOnRobot.ARM_BOTTOM] = 7
        positionToChannel[PositionOnRobot.ARM_RIGHT] = 8

        var servos = ArrayList<Servo>()

        for(positionOnRobot in PositionOnRobot.values()) {
            var toAdd = Servo()
            toAdd.setPositionOnRobot(positionOnRobot)
            toAdd.setServoPosition(getPositionOfServo(toAdd)!!)
            if (positionOnRobot.name.startsWith("HAND"))
            {
                toAdd.setCalibrationValueNotTurned(0.0)
                toAdd.setCalibrationValueTurned(0.7)
            }
            else {
                toAdd.setCalibrationValueNotTurned(0.08)
            }
            servos.add(toAdd)

        }

        Thread.sleep(1000)

        return servos

    }

    override fun moveArm(servo : Servo, positionOfServo : PositionOfServo)
    {
        var position = servo.getPositionOnRobot()
        if(!position.name.startsWith("ARM", false) || positionOfServo != PositionOfServo.OUTSIDE || positionOfServo != PositionOfServo.INSIDE)
        {
            println("Mouvement incorrect")
        }
        else
        {
            moveServo(servo, positionOfServo)
        }
    }

    override fun getPositionOfServo(servo : Servo) : PositionOfServo? {

        var position = servo.getPositionOnRobot()

        if(position.name.startsWith("ARM", true))
        {
            var channel = positionToChannel[position]!!
            var positionOfServo = servoSource.forChannel(channel).value
            if(servo.getCalibrationValueTurned() == positionOfServo) return PositionOfServo.OUTSIDE
            else if(positionOfServo == servo.getCalibrationValueNotTurned()) return PositionOfServo.INSIDE
            else return PositionOfServo.CURRENTLY_MOVING
        }
        else if(position.name.startsWith("HAND", true))
        {
            var channel = positionToChannel[position]!!
            var positionOfServo = servoSource.forChannel(channel).value
            if(servo.getCalibrationValueTurned() == positionOfServo) return PositionOfServo.TURNED
            else if(positionOfServo == servo.getCalibrationValueNotTurned()) return PositionOfServo.NOT_TURNED
            else return PositionOfServo.CURRENTLY_MOVING
        }
        else
        {
            println("get position of servo : unknown servo")
            return null
        }

    }

    override fun moveServo(servo: Servo, positionOfServo: PositionOfServo) {

        var command = getCommand(servo, positionOfServo)

        executor.execute(command)

        servo.setServoPosition(positionOfServo)

    }


    private fun getCommand(servo : Servo, positionOfServo: PositionOfServo) : ServoCommand<Int>
    {
        var newPosition : Double

        if(servo.getPositionOnRobot().name.startsWith("ARM") && positionOfServo == PositionOfServo.OUTSIDE
            || servo.getPositionOnRobot().name.startsWith("HAND") && positionOfServo == PositionOfServo.TURNED)
        {
            newPosition = servo.getCalibrationValueTurned()
        }
        else
        {
            newPosition = servo.getCalibrationValueNotTurned()
        }

        return ImmutableServoCommand(positionToChannel[servo.getPositionOnRobot()], newPosition, 1.0, 1.0)
    }

}