package app.model.robot

import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoIdentity

class Servo {

    private var calibrationValueNotTurned : Double = 0.0
    private var calibrationValueTurned : Double = 1.0

    private lateinit var positionOnRobot : ServoIdentity
    private lateinit var servoPosition : ServoState

    private var channel = -1

    fun isAnArm() : Boolean
    {
        return positionOnRobot.name.startsWith("ARM")
    }

    fun isAHand() : Boolean
    {
        return positionOnRobot.name.startsWith("HAND")
    }

    fun getChannel() : Int
    {
        return channel
    }

    fun setChannel(channel : Int)
    {
        this.channel = channel
    }

    fun setCalibrationValueNotTurned(value : Double)
    {
        this.calibrationValueNotTurned = value
    }

    fun setCalibrationValueTurned(value : Double)
    {
        this.calibrationValueTurned = value
    }

    fun getServoPosition() : ServoState
    {
        return servoPosition
    }

    fun setServoPosition(servoPosition : ServoState)
    {
        this.servoPosition = servoPosition
    }

    fun getPositionOnRobot() : ServoIdentity
    {
        return this.positionOnRobot
    }

    fun setPositionOnRobot(position : ServoIdentity)
    {
        this.positionOnRobot = position
    }

    fun getCalibrationValueNotTurned() : Double
    {
        return calibrationValueNotTurned
    }

    fun getCalibrationValueTurned() : Double
    {
        return calibrationValueTurned
    }
}