package app

import app.UI.ConsoleUI
import app.model.Color
import app.model.cube.Cube
import app.model.movement.Movement
import app.model.movement.RelativeMovement
import app.model.movement.RelativePosition
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.service.orientation.OrientationService
import app.service.robot.RobotOtvintaService
import app.solver.populationSolver.PopulationCrossSolver
import app.solver.Solver
import app.solver.ThistlethwaiteSolver.ThistlethwaiteSolver
import app.solver.bruteforceSolver.BruteforceOLLSolver
import app.solver.bruteforceSolver.BruteforcePLLSolver
import app.solver.populationSolver.PopulationCornerSolver
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

    //TODO RL
    //TODO Kociemba
    //TODO korf
    //TODO Genetic alg

    //TODO integrity check

    val cubeInitializationService : CubeInitializationService by inject()
    val cubeInformationService : CubeInformationService by inject()
    val cubeMotionService : CubeMotionService by inject()
    val consoleUI : ConsoleUI by inject()
    val orientationService : OrientationService by inject()
    val robotService : RobotOtvintaService by inject()
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    val useRobot = false
    val displayCube = true
    val printSolutions = true


    fun launch() {

        //KorfSolver().populateDatabase()

        val myOrientation = orientationService.getOrientation(Pair(RelativePosition.TOP, Color.WHITE), Pair(
            RelativePosition.FRONT, Color.GREEN))

        val movementService = MovementService()

        var cube = Cube(3, myOrientation)

        if(useRobot) robotService.init(myOrientation)

        if(useRobot) robotService.welcome()

        //cubeInitializationService.initCubeByKeyboard(cube)
        //cubeInitializationService.initCubeWithRobot(cube)
        //cubeInitializationService.initSolvedCube(cube)
        cubeInitializationService.initScrambledCube(cube, 50)

        if(displayCube) consoleUI.displayCube(cube)

        if(useRobot) robotService.release()

        var solution : Array<Movement>
        var totalSolution = arrayOf<Movement>()

       /* cubeMotionService.applySequence(cube, ThistlethwaiteSolver().stepOne(cube))

        for(i in 0..50)
        {
            var movements =          Movement.values().filter { it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(
                RelativeMovement.FRONT), cube.orientation)[0].name&&
                    it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT_REVERSE), cube.orientation)[0].name&&
                    it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK), cube.orientation)[0].name&&
                    it.name != movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK_REVERSE), cube.orientation)[0].name
            }.map { arrayOf(it) }
                .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.FRONT, RelativeMovement.FRONT), cube.orientation)))
                .plus(arrayOf(movementService.convertSequenceOfRelativeMovements(arrayOf(RelativeMovement.BACK, RelativeMovement.BACK), cube.orientation)))
                .toTypedArray()

            var appliedMovement = movements.random()

            cubeMotionService.applySequence(cube, appliedMovement)

            for(elem in appliedMovement)
            {
                println(elem)
            }

            for((edge, orientation) in cubeInformationService.getEdgeOrientations(cube))
            {
                print(orientation)
            }
            println()

        }
*/

        var solvers = arrayOf<Solver>(
            ThistlethwaiteSolver()
            //PopulationGroupSolver(100000, 0.001f, 7, true)
            /*PopulationCrossSolver(1000, 0.01f, 4, true),
            PopulationCornerSolver(1000, 0.01f, 4, true),
            PopulationSecondFloorSolver(1000, 0.01f, 4, true),
            BruteforceOLLSolver(cube),
            BruteforcePLLSolver(cube)*/
        )

        for(solver in solvers)
        {
            logger.info("Using " + solver.toString() + " on cube " + cube.toString())

            solution = solver.solve(cube)!!

            cubeMotionService.applySequence(cube, solution)

            totalSolution = totalSolution.plus(solution)

            if(printSolutions)
            {
                for(movement in solution)
                {
                    print(movement)
                    print(" ")
                }
            }
            if(displayCube) consoleUI.displayCube(cube)
        }

        logger.info("Total length of solution : " + totalSolution.size)

        if(useRobot) robotService.applySequence(totalSolution)
    }
}
