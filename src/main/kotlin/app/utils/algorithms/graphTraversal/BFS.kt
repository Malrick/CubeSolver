package app.utils.algorithms.graphTraversal

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.movement.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService

data class Node(var movements : Array<Movement>, var parent: Node?);

class BFS {

    var listOfMovements = arrayOf<Array<Movement>>()

    var position = listOf<Movement>()

    fun search(cube : Cube, goal : (Cube)-> Boolean) : Array<Movement>
    {
        var flag = false
        val queue = mutableListOf<Node>()
        queue.add(Node(arrayOf(), null))
        listOfMovements = Movement.values().map { arrayOf(it) }.toTypedArray()
        var i : Short = 0
        val cubeMotionService = CubeMotionService()
        val movementService = MovementService()
        var numberOfElemsToGo : Int
        var current = Node(arrayOf(), null)
        var last = current
        var clone = cube.clone()

        while(!flag) {

            current = queue.removeAt(0)

            if(!current.movements.isEmpty())
            {
                numberOfElemsToGo = findLink(last, current)

                if(numberOfElemsToGo==-1)
                {
                    cubeMotionService.applySequence(clone, current.movements)
                }
                else  {
                    cubeMotionService.applySequence(clone, movementService.getOppositeSequence(last.movements.takeLast(numberOfElemsToGo).toTypedArray()))
                    cubeMotionService.applySequence(clone, current.movements.takeLast(numberOfElemsToGo).toTypedArray())
                }

                if(goal(clone)) {
                    return current.movements
                }

                last = current
            }

            i++

            listOfMovements.forEach {
                queue.add(Node(current.movements + it, current))
            }
        }
        return arrayOf()
    }

    fun findLink(last : Node, current : Node) : Int
    {
        if(last.movements.size == current.movements.size)
        {
            var a = last
            var b = current
            var i = 1
            while(a.parent != b.parent)
            {
                a = a.parent!!
                b = b.parent!!
                i++
            }
            return i
        }
        if(last.movements.size < current.movements.size) return -1

        return 0
    }


}