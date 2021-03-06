package app.service.movement

import app.model.Color
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.model.orientation.RelativePosition
import app.model.orientation.Orientation
import org.koin.core.KoinComponent

class MovementService : KoinComponent {

    // Convert a sequence of relative movements to a sequence of absolute movements
    fun convertSequenceOfRelativeMovements(relativeSequence : Array<RelativeMovement>, orientation : Orientation) : Array<Movement>
    {
        var toReturn = arrayOf<Movement>()

        for(relativeMovement in relativeSequence)
        {
            var relativeMovementPosition = RelativePosition.values().first { relativeMovement.name.startsWith(it.name) }
            var antiClockwise = relativeMovement.name.endsWith("REVERSE")
            var double = relativeMovement.name.endsWith("DOUBLE")
            var colorOfMovement = orientation.colorPositions[relativeMovementPosition]!!

            var movement : Movement
            if(antiClockwise) movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE") }
            else if(double) movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && it.name.endsWith("DOUBLE") }
            else movement = Movement.values().first { it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE") && !it.name.endsWith("DOUBLE")}

            toReturn = toReturn.plus(movement)
        }

        return toReturn
    }

    // Convert a sequence of movements to an optimal sequence of movements
    // TODO doubles 180°
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
                        var colorOfFirstMovement = Color.values().first { toTreat[i].name.startsWith(it.name) }
                        var colorOfSecondMovement = Color.values().first { toTreat[i+1].name.startsWith(it.name) }
                        if(colorOfFirstMovement == colorOfSecondMovement)
                        {
                            wasTreated[i] = true
                            wasTreated[i+1] = true
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

    // Return false if the sequence of movements can be shortened (pruned)
    fun isOptimalSequence(sequence : Array<Movement>) : Boolean
    {
        for(i in sequence.indices)
        {
            if(i+1 < sequence.size)
            {
                var colorOfFirstMovement = Color.values().first { sequence[i].name.startsWith(it.name) }
                var colorOfSecondMovement = Color.values().first { sequence[i+1].name.startsWith(it.name) }
                if(colorOfFirstMovement == colorOfSecondMovement)
                {
                    return false
                }
            }
        }
        return true
    }

    // Used to do some pruning on the sequencer
    fun getSecondNonOptimalIndex(sequence: Array<Movement>) : Int
    {
        for(i in sequence.indices)
        {
            if(i+1 < sequence.size)
            {
                var colorOfFirstMovement = Color.values().first { sequence[i].name.startsWith(it.name) }
                var colorOfSecondMovement = Color.values().first { sequence[i+1].name.startsWith(it.name) }
                if(colorOfFirstMovement == colorOfSecondMovement)
                {
                    return i+1
                }
            }
        }
        return 0
    }

    // Used for the pruning on BFS / IDDFS
    fun getNextOptimalMovements(
        listOfMovements: Array<Array<Movement>>,
        movement: Movement?
    ) : Array<Array<Movement>>
    {
        if(movement == null) return listOfMovements

        var movements : Array<Array<Movement>>
        var colorOfMovement = Color.values().first { movement.name.startsWith(it.name) }!!

        movements = listOfMovements.filterNot { it.first().name.startsWith(colorOfMovement.name) }.toTypedArray()
        if(colorOfMovement == Color.RED || colorOfMovement == Color.YELLOW || colorOfMovement == Color.BLUE)
        {
            movements = movements.filterNot { it.first().name.startsWith(oppositeColor(colorOfMovement).name) }.toTypedArray()
        }
        return movements
    }

    // Get the reversed sequence of movements.
    fun getOppositeSequence(sequence : Array<Movement>) : Array<Movement>
    {
        return sequence.map{getOppositeMovement(it)}.reversed().toTypedArray()
    }

    // Get the opposite of a movement
    fun getOppositeMovement(movement : Movement) : Movement
    {
        var colorOfMovement = Color.values().first { movement.name.startsWith(it.name) }
        if(movement.name.endsWith("REVERSE")) return Movement.values().first{ it.name.startsWith(colorOfMovement.name) && !it.name.endsWith("REVERSE") && !it.name.endsWith("DOUBLE")}
        else if(movement.name.endsWith("DOUBLE")) return movement
        else return Movement.values().first{ it.name.startsWith(colorOfMovement.name) && it.name.endsWith("REVERSE")}
    }

    // Get the opposite of a color (only used for the above)
    private fun oppositeColor(color: Color) : Color
    {
        return when(color)
        {
            Color.WHITE -> Color.YELLOW
            Color.ORANGE -> Color.RED
            Color.GREEN -> Color.BLUE
            Color.RED -> Color.ORANGE
            Color.YELLOW -> Color.WHITE
            Color.BLUE -> Color.GREEN
        }
    }

}