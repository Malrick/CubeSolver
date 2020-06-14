package app.service.movement

import app.model.Color
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.model.movement.RelativePosition
import app.model.orientation.Orientation
import org.koin.core.KoinComponent

class MovementService : KoinComponent {

    fun convertSequenceOfRelativeMovements(relativeSequence : Array<RelativeMovement>, orientation : Orientation) : Array<Movement>
    {
        var toReturn = arrayOf<Movement>()

        for(relativeMovement in relativeSequence)
        {
            var relativeMovementPosition = RelativePosition.values().first { relativeMovement.name.startsWith(it.name) }
            var clockwise = !relativeMovement.name.endsWith("REVERSE")

            var colorOfMovement = orientation.colorPositions[relativeMovementPosition]!!

            var movement : Movement
            if(clockwise) movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE") }
            else movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE") }

            toReturn = toReturn.plus(movement)
        }

        return toReturn
    }

    // TODO doubles demi-tours
    fun convertToOptimalSequence(sequence : Array<Movement>) : Array<Movement>
    {
        var toTreat = sequence
        var wasTreated : Array<Boolean>
        var treatedValues : Array<Movement>

        do {
            treatedValues = arrayOf()
            wasTreated = Array(toTreat.size) {false}
            for(i in toTreat.indices)
            {
                if(wasTreated[i] == false)
                {
                    if(i+1 < toTreat.size)
                    {
                        if(toTreat[i] == getOpposite(toTreat[i+1]))
                        {
                            wasTreated[i] = true
                            wasTreated[i+1] = true
                            continue
                        }
                    }
                    if(i+2 < toTreat.size)
                    {
                        if(toTreat[i] == toTreat[i+1] && toTreat[i] == toTreat[i+2])
                        {
                            treatedValues += getOpposite(toTreat[i])
                            wasTreated[i] = true
                            wasTreated[i+1] = true
                            wasTreated[i+2] = true
                            continue
                        }
                    }
                    treatedValues += toTreat[i]
                }
            }
            toTreat = treatedValues
        } while (wasTreated.any { it.equals(true) })

        return treatedValues
    }

    fun isOptimalSequence(sequence : Array<Movement>) : Boolean
    {
        for(i in sequence.indices)
        {
            if(i+1 < sequence.size)
            {
                if(sequence[i] == getOpposite(sequence[i+1]))
                {
                    return false
                }
            }
            if(i+2 < sequence.size)
            {
                if(sequence[i] == sequence[i+1] && sequence[i] == sequence[i+2])
                {
                    return false
                }
            }
        }
        return true
    }

    fun getOpposite(movement : Movement) : Movement
    {
        var colorOfMovement = Color.values().first { movement.name.startsWith(it.name) }
        if(movement.name.endsWith("REVERSE")) return Movement.values().first{ it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE")}
        else return Movement.values().first{ it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE")}
    }

}