package app.solver.ThistlethwaiteSolver
import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.database.Database
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.solver.Solver
import app.utils.algorithms.graphTraversal.IDDFS
import app.utils.database.DatabaseUtils
import app.utils.files.CsvUtils
import app.utils.indexing.LehmerRanker
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

class ThistlethwaiteSolver : Solver, KoinComponent{

    // Services
    private val cubeInformationService : CubeInformationService by inject()
    private val cubeMotionService : CubeMotionService by inject()
    private val movementService : MovementService by inject()
    private val csvUtils : CsvUtils by inject()

    private val databaseUtils : DatabaseUtils by inject()
    private val lehmerRanker : LehmerRanker by inject()

    private val iddfs : IDDFS by inject()

    // Saving a list of 96 positions index (lehmer codes) where corners are in orbits (if well oriented)
    private var listOrbit = listOf<Int>()

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
            it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BOTTOM_REVERSE), Cube.orientation)[0].name ||
            it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT), Cube.orientation)[0].name          ||
            it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), Cube.orientation)[0].name  ||
            it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), Cube.orientation)[0].name         ||
            it.name == movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), Cube.orientation)[0].name
    }.map { arrayOf(it) }
        .toTypedArray()}

    var G4 = Movement.values().filter { it.name.endsWith("DOUBLE") }.map { arrayOf(it) }.toTypedArray()

    var wellOrientedEdges   : (Cube) -> Boolean = { Cube -> cubeInformationService.getEdgeOrientations(Cube).all { it == 1 }}
    var wellOrientedCorners : (Cube) -> Boolean = { Cube -> cubeInformationService.getCornerOrientations(Cube).all { it == 0 }}
    var cornersInOrbits     : (Cube) -> Boolean = { Cube -> listOrbit.contains(lehmerRanker.lookupCornersPosition(Cube))}
    var allCornersAreSolved : (Cube) -> Boolean = { Cube -> cubeInformationService.allCornersAreSolved(Cube)}
    var allMEdgesInMSlice   : (Cube) -> Boolean = { Cube -> cubeInformationService.allMEdgesInMSlice(Cube)}
    var allEdgeToTheirSlice : (Cube) -> Boolean = { Cube -> cubeInformationService.allEdgesToTheirSlices(Cube)}

    override fun solve(cube: Cube): Array<Movement>? {

        var clone = cube.clone()

        listOrbit += databaseUtils.getRecords(Database.CORNERS_ORBITS).keys.map { it.toInt() }

        var solution = arrayOf<Movement>()
        var subSolution = arrayOf<Movement>()

        var solutionLengths = listOf<Int>()
        var times = listOf<Long>()

        for(i in 1..5)
        {
            var elapsedTime = measureTimeMillis { subSolution = step(i, clone)}
            times += elapsedTime
            solutionLengths += subSolution.size

            //logger.info("step $i solved in " + (elapsedTime/1000.0).toString()+" seconds, and ${subSolution.size} movements")

            solution += subSolution

            cubeMotionService.applySequence(clone, subSolution)
        }
        logger.info("Total solving time : " + times.sum()/1000.0 + "s")
        csvUtils.exportThistlethwaiteValuesToCsv("thistlethwaite-analysis", times, solutionLengths)
        logger.info("Solving times and solution lengths exported to csv file.")

        return solution
    }

    fun step(i : Int, cube:Cube) : Array<Movement>
    {
        // Init seach space
        iddfs.init(when(i)
        {
            1 -> G1
            2 -> G2(cube)
            3 -> G3(cube)
            4 -> G3(cube)
            else -> G4
        })

        // Goal definition
        var goal : (Cube) -> Boolean = when(i)
        {
            1 -> wellOrientedEdges
            2 -> {Cube -> wellOrientedCorners(Cube) && allMEdgesInMSlice(Cube)}
            3 -> {Cube -> cornersInOrbits(Cube)}
            4 -> {Cube -> cornersInOrbits(Cube) && allEdgeToTheirSlice(Cube)}
            else -> {Cube -> cubeInformationService.isSolved(Cube)}
        }

        // Search
        return iddfs.search(cube, goal)
    }
}