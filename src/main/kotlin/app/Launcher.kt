package app

import app.UI.ConsoleUI
import app.model.Color
import app.model.cube.Cube
import app.model.movement.Movement
import app.model.movement.RelativePosition
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.service.orientation.OrientationService
import app.service.robot.RobotOtvintaService
import app.solver.bruteforceSolver.BruteforcePLLSolver
import app.solver.populationSolver.PopulationCornerSolver
import app.solver.populationSolver.PopulationCrossSolver
import app.solver.Solver
import app.solver.bruteforceSolver.BruteforceOLLSolver
import app.solver.populationSolver.PopulationSecondFloorSolver
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.opencv.core.Core
import org.slf4j.LoggerFactory


fun main()
{
    startKoin{modules(listOf(serviceModule, displayModule, visionModule, utilsModule))}
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    Launcher().launch()
}

class Launcher : KoinComponent {

    //TODO Nettoyage / responsabilités
    //TODO Exceptions
    //TODO Multithreading
    //TODO Refacto motion

    //TODO debug population solver

    //TODO RL
    //TODO Kociemba
    //TODO korf
    //TODO Genetic alg

    //TODO integrity check

    private val cubeInitializationService : CubeInitializationService by inject()
    private val cubeMotionService : CubeMotionService by inject()
    private val consoleUI : ConsoleUI by inject()
    private val orientationService : OrientationService by inject()
    private val robotService : RobotOtvintaService by inject()
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    val useRobot = false
    val displayCube = true
    val displaySolutions = true


    fun launch() {

        //KorfSolver().populateDatabase()

        val myOrientation = orientationService.getOrientation(Pair(RelativePosition.TOP, Color.BLUE), Pair(
            RelativePosition.FRONT, Color.YELLOW))

        val movementService = MovementService()

        var cube = Cube(3, myOrientation)

        if(useRobot) robotService.init(myOrientation)

        //cubeInitializationService.initCubeByKeyboard(cube)
        cubeInitializationService.initCubeWithRobot(cube)
        //cubeInitializationService.initSolvedCube(cube)
        //cubeInitializationService.initScrambledCube(cube, 20)

        if(displayCube) consoleUI.displayCube(cube)

        /*var newBFS = newBFS()

        newBFS.search(cube) { Cube -> cubeInformationService.isSolved(cube)}*/

        var solution : Array<Movement>
        var totalSolution = arrayOf<Movement>()

        var solvers = arrayOf<Solver>(
            //ThistlethwaiteSolver()
            //PopulationGroupSolver(100000, 0.001f, 7, true)
            PopulationCrossSolver(500, 0.01f, 7, true),
            PopulationCornerSolver(100, 0.1f, 4, true),
            PopulationSecondFloorSolver(100, 0.1f, 4, true),
            BruteforceOLLSolver(cube),
            BruteforcePLLSolver(cube)
        )

        for(solver in solvers)
        {
            logger.info("Using " + solver.toString() + " on cube " + cube.toString())

            solution = solver.solve(cube)!!

            cubeMotionService.applySequence(cube, solution)

            totalSolution = totalSolution.plus(solution)

            if(displaySolutions) consoleUI.displaySequence(solution)

            if(displayCube) consoleUI.displayCube(cube)
        }

        logger.info("Total length of solution : " + totalSolution.size)

        if(useRobot) robotService.applySequence(totalSolution)

    }
}
