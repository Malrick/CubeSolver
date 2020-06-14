package app.utils.algorithms

import app.model.movement.Movement
import app.service.movement.MovementService

class BFS {

    val movementService = MovementService()

    var counter = 0

    var wasteCounter = 0

    var listOfMovements = arrayOf<Array<Movement>>()

    // Initialization of the population : BFS
    var currentPosition = arrayOf(0)
    // ,7,2,0,11,9,3,2

    fun init(listOfMovements : Array<Array<Movement>>)
    {
        this.listOfMovements = listOfMovements
        currentPosition = arrayOf(0)
    }

    fun getElements(numberOfElements : Int) : Array<Array<Movement>>
    {
        var toReturn = arrayOf<Array<Movement>>()
        for(i in 0..numberOfElements)
        {
            toReturn += getElement()
        }
        return toReturn
    }

    fun getElement() : Array<Movement>
    {
        //add an element at coordinates [currentDepth, currentBreath]
        var toReturn = arrayOf<Movement>()

        for (position: Int in currentPosition) {
            var toAdd = listOfMovements.elementAt(position)
            toReturn += toAdd
        }

        while(!movementService.isOptimalSequence(toReturn))
        {
            toReturn = arrayOf()
            counter++
            wasteCounter++
            iterate()
            for (position: Int in currentPosition) {
                var toAdd = listOfMovements.elementAt(position)
                toReturn += toAdd
            }
        }

        iterate()

        counter++

        return toReturn
    }

    fun iterate()
    {
        var breadth = currentPosition.size - 1
        // TODO simplifier

        // Going a step further
        currentPosition[breadth]++

        // If we are arrived to the last element
        if (currentPosition[breadth] == listOfMovements.size) {
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
                    if (currentPosition[breadth] == listOfMovements.size) {
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
    }
}