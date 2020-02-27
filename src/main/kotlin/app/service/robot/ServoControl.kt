package app.service.robot

import app.model.robot.Servo
import app.model.robot.constants.PositionOfServo

interface ServoControl {

    fun moveServo(servo : Servo, positionOfServo: PositionOfServo)

    fun getPositionOfServo(servo : Servo) : PositionOfServo?

}