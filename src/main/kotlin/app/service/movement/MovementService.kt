package app.service.movement

import app.model.Color
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.model.movement.RelativePosition
import org.koin.core.KoinComponent

class MovementService : KoinComponent {

    fun convertSequenceOfRelativeMovements(relativeSequence : Array<RelativeMovement>, orientation : HashMap<RelativePosition, Color>) : Array<Movement>
    {
        var toReturn = arrayOf<Movement>()

        for(relativeMovement in relativeSequence)
        {
            var relativeMovementPosition = RelativePosition.values().first { relativeMovement.name.startsWith(it.name) }
            var clockwise = !relativeMovement.name.endsWith("REVERSE")

            var colorOfMovement = orientation[relativeMovementPosition]!!

            var movement : Movement
            if(clockwise) movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE") }
            else movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE") }

            toReturn = toReturn.plus(movement)
        }

        return toReturn
    }

}