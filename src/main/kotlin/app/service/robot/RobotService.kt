package app.service.robot

import app.model.Color
import app.model.movement.Movement
import app.model.orientation.RelativePosition
import app.model.orientation.Orientation
import org.opencv.core.Mat
import java.util.HashMap


/*
    If other robots are used on the project, the drivers will have to implement this interface
 */
interface RobotService {

    fun init(orientation : Orientation)

    fun release()

    fun welcome()

    fun turnCube(positionOfCubeEnum: RelativePosition)

    fun takePicturesAndGetColors() : HashMap<Color, Array<Mat>>

    fun applySequence(sequence : Array<Movement>)

    fun applyMovement(movement: Movement)
}