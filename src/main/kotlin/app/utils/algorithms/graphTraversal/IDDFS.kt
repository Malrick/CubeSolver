package app.utils.algorithms.graphTraversal

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.cube.position.CornerPosition
import app.model.database.Database
import app.model.movement.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.solver.KorfSolver.KorfSolver
import app.utils.database.DatabaseUtils
import app.utils.indexing.LehmerRanker
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory
import java.util.*

class IDDFS : KoinComponent{

    var listOfMovements = arrayOf<Array<Movement>>()

    var position = listOf<Movement>()

    val cubeInformationService : CubeInformationService by inject()
    val cubeMotionService = CubeMotionService()
    val movementService = MovementService()

    private val logger = LoggerFactory.getLogger(IDDFS::class.java)

    fun init(movements : Array<Array<Movement>>)
    {
        this.listOfMovements = movements
    }

    fun search(cube: Cube, goal: (Cube)-> Boolean) : Array<Movement>
    {
        var stack = Stack<Node>()
        var current = Node(arrayOf(), null)
        var last = current
        var numberOfElemsToGo : Int
        var maxDepht = 1
        var clone = cube.clone()
        stack.push(Node(arrayOf(), null))

        if(goal(cube)) return arrayOf()

        logger.debug("Starting the research...")

        while(true) {

            current = stack.pop()

            if(current.movements.size == maxDepht)
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

                if(stack.isEmpty())
                {
                    maxDepht++
                    stack.push(Node(arrayOf(), null))
                    //logger.info("maxDepht = $maxDepht")
                    //logger.info(listLehmer.size.toString())
                    cubeMotionService.applySequence(clone, movementService.getOppositeSequence(current.movements))
                }
            }
            else if(current.movements.size<maxDepht)
            {
                movementService.getNextOptimalMovements(listOfMovements, current.movements.lastOrNull()).forEach {
                    stack.push(Node(current.movements + it, current))
                }
            }
        }
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