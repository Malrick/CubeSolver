package app.model.robot

import app.model.robot.constants.ServoState
import app.model.robot.constants.ServoPositions

class Servo {

    private var calibrationValueNotTurned : Double = 0.0
    private var calibrationValueTurned : Double = 1.0

    private lateinit var positionOnRobot : ServoPositions
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

    fun getServoPosition() : ServoState
    {
        return servoPosition
    }

    fun setServoPosition(servoPosition : ServoState)
    {
        this.servoPosition = servoPosition
    }

    fun getPositionOnRobot() : ServoPositions
    {
        return this.positionOnRobot
    }

    fun setPositionOnRobot(position : ServoPositions)
    {
        this.positionOnRobot = position
    }

    fun getCalibrationValueNotTurned() : Double
    {
        return calibrationValueNotTurned
    }

    fun setCalibrationValueNotTurned(value : Double)
    {
        this.calibrationValueNotTurned = value
    }

    fun setCalibrationValueTurned(value : Double)
    {
        this.calibrationValueTurned = value
    }

    fun getCalibrationValueTurned() : Double
    {
        return calibrationValueTurned
    }
}