package app.utils.algorithms.graphTraversal

import app.model.movement.Movement
import app.service.movement.MovementService

class IDDFS {

    val movementService = MovementService()

    var counter = 0

    var wasteCounter = 0

    var listOfMovements = arrayOf<Array<Movement>>()

    var currentPosition = arrayOf(0)

    var currentDepth = 1

    var maxDepth = 1

    var pruning: () -> Int = {movementService.getSecondNonOptimalIndex(compose())}

    fun compose(): Array<Movement> {
        var toReturn = arrayOf<Movement>()
        for (position: Int in currentPosition) {
            try {
                var toAdd = listOfMovements.elementAt(position)
                toReturn += toAdd
            }
            catch (e : Exception)
            {
                print("bonjour")
            }
        }
        return toReturn
    }

    fun init(listOfMovements : Array<Array<Movement>>)
    {
        this.listOfMovements = listOfMovements
        currentPosition = arrayOf(0)
    }

    fun getElements(numberOfElements : Int) : Array<Array<Movement>>
    {
        var toReturn = arrayOf<Array<Movement>>()
        for(i in 0 until numberOfElements)
        {
            toReturn += getElement()
        }
        return toReturn
    }

    fun getElement() : Array<Movement>
    {
        iterate()

        counter++

        return compose()
    }

    fun iterate()
    {
        do {
            if(currentPosition.size < currentDepth)
            {
                currentPosition += 0
                return
            }
            else if (currentPosition.all { it==listOfMovements.size-1 })
            {
                currentDepth++
                currentPosition = arrayOf()
            }
            else
            {
                if(pruning()!=0)
                {
                    currentPosition[movementService.getSecondNonOptimalIndex(compose())]++
                }
                else
                {
                    currentPosition[currentPosition.lastIndex]++
                }

                var resetVariable = currentPosition.lastIndex
                do {
                    if(currentPosition[resetVariable] >= listOfMovements.size)
                    {
                        currentPosition[resetVariable] = 0
                        if(resetVariable==0)
                        {
                            currentDepth++
                        }
                        if(resetVariable>0)
                        {
                            currentPosition[resetVariable-1] ++
                        }
                    }
                    resetVariable--
                }while(resetVariable>=0)

            }
        } while(pruning()!=0)
    }

}