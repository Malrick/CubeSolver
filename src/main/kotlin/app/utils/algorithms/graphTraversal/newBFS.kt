package app.utils.algorithms.graphTraversal

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.movement.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService

data class Node(var movements : Array<Movement>, var parent: Node?);

class newBFS {

    var listOfMovements = arrayOf<Array<Movement>>()

    var position = listOf<Movement>()

    //
    fun search(cube : Cube, goal : (Cube)-> Boolean)
    {
        var flag = false
        val queue = mutableListOf<Node>()
        queue.add(Node(arrayOf(), null))
        listOfMovements = Movement.values().map { arrayOf(it) }.toTypedArray()
        var i = 0
        val cubeMotionService = CubeMotionService()
        val movementService = MovementService()
        while(!flag) {
            val current = queue.removeAt(0)

            /*if(!current.movements.isEmpty())
            {
                var moves = current.movements
                cubeMotionService.applySequence(cube, moves)

                if(goal(cube))
                {
                    flag = true
                    ConsoleUI().displayCube(cube)
                }

                cubeMotionService.applySequence(cube, movementService.getOpposite(moves))
            }*/

            i++

            listOfMovements.forEach {
                queue.add(Node(current.movements + it, current))
            }
        }

    }


}