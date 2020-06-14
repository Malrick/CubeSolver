package app.utils

import app.model.movement.Movement
import app.service.movement.MovementService
import org.koin.core.KoinComponent

class AlgorithmUtils : KoinComponent {

    val movementService = MovementService()

    fun BFS(numberOfWantedElements: Int, returnAllUntilLength : Int = 0 , movements: Set<Array<Movement>>) : Set<Array<Movement>>
    {
        var counter = 0
        var results = arrayOf<Array<Movement>>()
        var wasteCounter = 0

        // Initialization of the population : BFS
        var currentPosition = arrayOf(0)

        var elementToAdd  = arrayOf<Movement>()

        while (counter < numberOfWantedElements || (returnAllUntilLength!= 0 && elementToAdd.size<returnAllUntilLength)) {

            //add an element at coordinates [currentDepth, currentBreath]
            var elementToAdd = arrayOf<Movement>()

            for (current: Int in currentPosition) {
                var toAdd = movements.elementAt(current)
                elementToAdd = elementToAdd.plus(toAdd)
            }

            if(movementService.convertToOptimalSequence(elementToAdd).size == elementToAdd.size)
            {
                results += elementToAdd
            }
            else
            {
                counter--
                wasteCounter++
            }

            var breadth = currentPosition.size - 1
            // TODO simplifier

            // Going a step further
            currentPosition[breadth]++

            // If we are arrived to the last element
            if (currentPosition[breadth] == movements.size) {
                // We get back to the first one
                currentPosition[breadth] = 0

                // And we add an element if it was the first time
                if (breadth == 0) {
                    currentPosition += 0
                }

                // If it was not the first time
                if (breadth > 0) {
                    // The position of the element before is increased
                    currentPosition[breadth - 1]++

                    // Using breadth as a counter
                    while (breadth >= 0) {
                        // If the position of the considered breadth is the end
                        if (currentPosition[breadth] == movements.size) {
                            // We get it back to zero
                            currentPosition[breadth] = 0
                            // if there's some stuff to the left, -1
                            if (breadth > 0) {
                                currentPosition[breadth - 1]++
                            } else {
                                // If not, add an element
                                currentPosition += 0
                            }
                        }
                        breadth--
                    }
                }
            }
            counter++
        }

        return results.toSet()
    }




}