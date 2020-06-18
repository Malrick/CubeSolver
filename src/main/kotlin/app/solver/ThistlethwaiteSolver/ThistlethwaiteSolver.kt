package app.solver.ThistlethwaiteSolver
import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.cube.position.CornerPosition
import app.model.cube.position.Position
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.model.movement.RelativePosition
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.solver.Solver
import app.utils.algorithms.graphTraversal.BFS
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory

class ThistlethwaiteSolver : Solver, KoinComponent{

    // Services / Helpers
    private val cubeInformationService : CubeInformationService by inject()
    private val cubeMotionService : CubeMotionService by inject()
    private val movementService : MovementService by inject()

    private val bfs : BFS by inject()

    private val logger = LoggerFactory.getLogger(ThistlethwaiteSolver::class.java)

    var G1 = Movement.values().map { arrayOf(it) }.toTypedArray()

    var G2 : (Cube) -> Array<Array<Movement>> = { Cube -> Movement.values().filterNot { it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.TOP), Cube.orientation)[0].name         ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.TOP_REVERSE), Cube.orientation)[0].name ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM), Cube.orientation)[0].name          ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM_REVERSE), Cube.orientation)[0].name
                                                                                        }.map { arrayOf(it) }
                                                                                         .toTypedArray()}

    var G3 : (Cube) -> Array<Array<Movement>> = { Cube -> Movement.values().filterNot { it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.TOP), Cube.orientation)[0].name         ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.TOP_REVERSE), Cube.orientation)[0].name ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM), Cube.orientation)[0].name          ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM_REVERSE), Cube.orientation)[0].name
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT), Cube.orientation)[0].name          ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), Cube.orientation)[0].name  ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), Cube.orientation)[0].name         ||
                                                                                        it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), Cube.orientation)[0].name
                                                                                        }.map { arrayOf(it) }
                                                                                         .toTypedArray()}

    var G4 = Movement.values().filter { it.name.endsWith("DOUBLE") }.map { arrayOf(it) }.toTypedArray()

    var wellOrientedEdges : (Cube) -> Boolean = { Cube -> cubeInformationService.getEdgeOrientations(Cube).all { it.value == 1 }}
    var wellOrientedCorners : (Cube) -> Boolean = { Cube -> cubeInformationService.getCornerOrientations(Cube).all { it.value == 0 }}
    var cornersInOrbits : (Cube) -> Boolean = {Cube -> Cube.positions.keys.filter { it is CornerPosition && cornerIsSuitable(Cube, it)  }.size == 8}
    var allEdgeToTheirSlice : (Cube) -> Boolean = { Cube -> allMEdgesInMSlice(Cube) && allEEdgesInESlice(Cube) && allSEdgesInSSlice(Cube) && cornersInOrbits(Cube)}

    var trackedPositionsM : (Cube) -> Set<Position> = { Cube -> cubeInformationService.getPositionsOfEdges(Cube, setOf(Pair(Cube.orientation.colorPositions[RelativePosition.TOP]!!, Cube.orientation.colorPositions[RelativePosition.FRONT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.FRONT]!!,Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.BACK]!!,Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.BACK]!!,Cube.orientation.colorPositions[RelativePosition.TOP]!!)))}

    var trackedPositionsE : (Cube) -> Set<Position> = { Cube -> cubeInformationService.getPositionsOfEdges(Cube, setOf(Pair(Cube.orientation.colorPositions[RelativePosition.FRONT]!!, Cube.orientation.colorPositions[RelativePosition.LEFT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.FRONT]!!,Cube.orientation.colorPositions[RelativePosition.RIGHT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.BACK]!!,Cube.orientation.colorPositions[RelativePosition.LEFT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.BACK]!!,Cube.orientation.colorPositions[RelativePosition.RIGHT]!!)))}

    var trackedPositionsS : (Cube) -> Set<Position> = { Cube -> cubeInformationService.getPositionsOfEdges(Cube, setOf(Pair(Cube.orientation.colorPositions[RelativePosition.TOP]!!, Cube.orientation.colorPositions[RelativePosition.LEFT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.TOP]!!,Cube.orientation.colorPositions[RelativePosition.RIGHT]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.LEFT]!!,Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!),
                                                                                                                       Pair(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!,Cube.orientation.colorPositions[RelativePosition.RIGHT]!!)))}

    var allMEdgesInMSlice : (Cube) -> Boolean = {Cube -> trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!)    && it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)}
                                                      && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)  && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
                                                      && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!)   && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
                                                      && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!)   && it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) }}

    var allEEdgesInESlice : (Cube) -> Boolean = {Cube -> trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)  && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!)}
                                                      && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)  && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }
                                                      && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!)   && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!) }
                                                      && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!)   && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }}

    var allSEdgesInSSlice : (Cube) -> Boolean = {Cube -> trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!)    && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!)}
                                                      && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!)    && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }
                                                      && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!)   && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
                                                      && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }}

    // Studied set
    private lateinit var listOfMovements : Array<Array<Movement>>

    override fun solve(cube: Cube): Array<Movement>? {

        var solution = arrayOf<Movement>()
        var subSolution = arrayOf<Movement>()
        var clone = cube.clone()
        var consoleUI = ConsoleUI()

        for(i in 1..6)
        {
            subSolution = when(i)
            {
                1-> stepOne(clone)
                2-> stepTwo(clone)
                4-> stepThree(clone)
                5-> stepFour(clone)
                6-> stepFive(clone)
                else -> throw Exception()
            }

            println()
            logger.info("step $i solved !")
            solution += subSolution
            cubeMotionService.applySequence(clone, subSolution)
            consoleUI.displayCube(clone)
        }

        return solution
    }

    fun stepOne(cube : Cube) : Array<Movement>
    {
        listOfMovements = G1

        return findSolution(wellOrientedEdges, cube)
    }

    fun stepTwo(cube : Cube) : Array<Movement>
    {
        listOfMovements = G2(cube)

        var goal : (Cube) -> Boolean = {Cube -> allMEdgesInMSlice(Cube) && wellOrientedCorners(Cube)}

        return findSolution(goal, cube)

    }

    fun stepThree(cube : Cube) : Array<Movement>
    {
        listOfMovements  = G3(cube)

        var goal : (Cube) -> Boolean = {Cube -> allMEdgesInMSlice(Cube) && cornersInOrbits(Cube) &&  wellOrientedCorners(Cube)}

        return findSolution(goal, cube)

    }

    fun stepFour(cube : Cube) : Array<Movement>
    {
        listOfMovements  = G3(cube)

        var goal : (Cube) -> Boolean = {Cube -> cornersInOrbits(Cube) && allEdgeToTheirSlice(Cube)}

        return findSolution(goal, cube)

    }

    fun stepFive(cube : Cube) : Array<Movement>
    {
        listOfMovements = G4

        var goal : (Cube) -> Boolean = {Cube -> cubeInformationService.isSolved(Cube)}

        return findSolution(goal, cube)
    }

    fun cornerIsSuitable(cube : Cube, corner : CornerPosition) : Boolean
    {
        if(cubeInformationService.isCornerOfAColorSolved(cube, corner.colorOne, corner.colorTwo, corner.colorThree)) return true
        if(cube.positions[corner]!!.getColors().intersect(corner.getColors().map { cubeInformationService.getOppositeColor(cube, it) }).size ==2) return true
        return false
    }

    fun findSolution(finishingCondition: (Cube) -> Boolean, cube: Cube): Array<Movement>
    {
        bfs.init(listOfMovements)

        var cubeExperiment = cube.clone()

        var sequence = arrayOf<Movement>()

        while(!finishingCondition(cubeExperiment))
        {
            cubeExperiment = cube.clone()

            sequence = bfs.getElement()

            cubeMotionService.applySequence(cubeExperiment, sequence)
        }

        return sequence
    }


}