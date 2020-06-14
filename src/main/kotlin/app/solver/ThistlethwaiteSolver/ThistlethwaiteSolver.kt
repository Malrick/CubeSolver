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
import app.utils.algorithms.BFS
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

    var wellOrientedEdges : (Cube) -> Boolean = { Cube -> cubeInformationService.getEdgeOrientations(Cube).all { it.value == 1 }}
    var WellOrientedCorners : (Cube) -> Boolean = { Cube -> cubeInformationService.getCornerOrientations(Cube).all { it.value == 0 }}
    var cornersInOrbits : (Cube) -> Boolean = {Cube -> Cube.positions.keys.filter { it is CornerPosition && cornerIsSuitable(Cube, it)  }.size == 8}
    var AllEdgeToTheirSlice : (Cube) -> Boolean = { Cube -> allMEdgesInMSlice(Cube) && allEEdgesInESlice(Cube) && allSEdgesInSSlice(Cube) && cornersInOrbits(Cube)}

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

    var allMEdgesInMSlice : (Cube) -> Boolean = {Cube -> trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)}
            && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
            && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
            && trackedPositionsM(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) }}

    var allEEdgesInESlice : (Cube) -> Boolean = {Cube -> trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!)}
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!) }
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }}

    var allSEdgesInSSlice : (Cube) -> Boolean = {Cube -> trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!)}
            && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }
            && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.LEFT]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
            && trackedPositionsS(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.RIGHT]!!) }}

    var toCheck : (Cube) -> Boolean = {Cube -> trackedPositionsE    (Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!)}
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.FRONT]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.BOTTOM]!!) }
            && trackedPositionsE(Cube).any { it.possessColor(Cube.orientation.colorPositions[RelativePosition.BACK]!!) && it.possessColor(Cube.orientation.colorPositions[RelativePosition.TOP]!!) }}

    // Studied set
    private lateinit var listOfMovements : Array<Array<Movement>>

    override fun solve(cube: Cube): Array<Movement>? {

        var solution = arrayOf<Movement>()
        var subSolution = arrayOf<Movement>()
        var clone = cube.clone()
        var consoleUI = ConsoleUI()

        subSolution = stepOne(cube)

        logger.info("step one solved")

        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        subSolution = stepTwo(clone)

        logger.info("step two solved")

        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        subSolution = stepThree(clone)

        logger.info("step three solved")

        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        subSolution = stepFour(clone)

        logger.info("step four solved")


        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        subSolution = stepFive(clone)

        logger.info("step five solved")

        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        subSolution = stepSix(clone)

        logger.info("step six solved")

        solution += subSolution

        cubeMotionService.applySequence(clone, subSolution)

        consoleUI.displayCube(clone)

        return solution
    }

    fun stepOne(cube : Cube) : Array<Movement>
    {
        listOfMovements = Movement.values().map { arrayOf(it) }.toTypedArray()

        return findSolution(wellOrientedEdges, cube)
    }

    fun stepTwo(cube : Cube) : Array<Movement>
    {
        listOfMovements  = Movement.values().filter { it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT), cube.orientation)[0].name   &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), cube.orientation)[0].name
        }.map { arrayOf(it) }
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))
            .toTypedArray()

        var goal : (Cube) -> Boolean = {Cube -> wellOrientedEdges(Cube) &&  allEEdgesInESlice(Cube)}

        return findSolution(goal, cube)

    }

    fun stepThree(cube : Cube) : Array<Movement>
    {
        listOfMovements = Movement.values().filter { it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(
            RelativeMovement.FRONT), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), cube.orientation)[0].name
        }.map { arrayOf(it) }
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))
            .toTypedArray()

        var goal : (Cube) -> Boolean = {Cube -> wellOrientedEdges(Cube)  &&  allEEdgesInESlice(Cube) && WellOrientedCorners(Cube)}

        return findSolution(goal, cube)
    }

    fun stepFour(cube : Cube) : Array<Movement>
    {
        listOfMovements  = Movement.values().filter { it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT_REVERSE), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT_REVERSE), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), cube.orientation)[0].name
        }.map { arrayOf(it) }
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT, RelativeMovement.LEFT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT, RelativeMovement.RIGHT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))
            .toTypedArray()

        var goal : (Cube) -> Boolean = {Cube -> wellOrientedEdges(Cube) && WellOrientedCorners(Cube) && allEEdgesInESlice(Cube) && cornersInOrbits(Cube)}

        return findSolution(goal, cube)

    }

    fun stepFive(cube : Cube) : Array<Movement>
    {
        listOfMovements  = Movement.values().filter { it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT_REVERSE), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT_REVERSE), cube.orientation)[0].name&&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), cube.orientation)[0].name &&
                it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), cube.orientation)[0].name
        }.map { arrayOf(it) }
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT, RelativeMovement.LEFT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT, RelativeMovement.RIGHT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))
            .toTypedArray()

        var goal : (Cube) -> Boolean = {Cube -> wellOrientedEdges(Cube) && WellOrientedCorners(Cube) && allEEdgesInESlice(Cube) && cornersInOrbits(Cube) && AllEdgeToTheirSlice(Cube)}

        return findSolution(goal, cube)

    }

    fun stepSix(cube : Cube) : Array<Movement>
    {
        var goal : (Cube) -> Boolean = {Cube -> cubeInformationService.isSolved(Cube)}

        listOfMovements = arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.LEFT, RelativeMovement.LEFT), cube.orientation))
                .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.RIGHT, RelativeMovement.RIGHT), cube.orientation)))
                .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.TOP, RelativeMovement.TOP), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM, RelativeMovement.BOTTOM), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
            .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))

        return findSolution(goal, cube)
    }

    fun cornerIsSuitable(cube : Cube, corner : CornerPosition) : Boolean
    {
        var newBFS = BFS()

        newBFS.init(arrayOf(arrayOf(Movement.BLUE, Movement.BLUE),
            arrayOf(Movement.GREEN, Movement.GREEN),
            arrayOf(Movement.RED, Movement.RED),
            arrayOf(Movement.ORANGE, Movement.ORANGE),
            arrayOf(Movement.WHITE, Movement.WHITE),
            arrayOf(Movement.YELLOW, Movement.YELLOW)))

        var sequence = arrayOf<Movement>()

        if(corner.getColors() == cube.positions[corner]!!.getColors()) {
            return true
        }
        while(sequence.size < 3)
        {
            sequence = newBFS.getElement()

            var clone = cube.clone()

            cubeMotionService.applySequence(clone, sequence)

            if(corner.getColors() == clone.positions[corner]!!.getColors()) return true
        }
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