package app.model.robot

import app.model.robot.constants.PositionOfServo
import app.model.robot.constants.PositionOnRobot

class Servo {

    private var calibrationValueNotTurned : Double = 0.0
    private var calibrationValueTurned : Double = 1.0

    private lateinit var positionOnRobot : PositionOnRobot
    private lateinit var servoPosition : PositionOfServo

    private var channel = -1

    fun isAnArm() : Boolean
    {
        return positionOnRobot.name.startsWith("ARM", true)
    }

    fun isAHand() : Boolean
    {
        return positionOnRobot.name.startsWith("HAND", true)
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


    fun getServoPosition() : PositionOfServo
    {
        return servoPosition
    }

    fun setServoPosition(servoPosition : PositionOfServo)
    {
        this.servoPosition = servoPosition
    }

    fun getPositionOnRobot() : PositionOnRobot
    {
        return this.positionOnRobot
    }

    fun setPositionOnRobot(position : PositionOnRobot)
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