package app

import app.UI.ConsoleUI
import app.model.Color
import app.model.cube.Cube
import app.model.movement.Movement
import app.model.orientation.RelativePosition
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.orientation.OrientationService
import app.service.robot.RobotOtvintaService
import app.solver.exhaustiveSolver.ExhaustiveOLLSolver
import app.solver.exhaustiveSolver.ExhaustivePLLSolver
import app.solver.populationSolver.PopulationCornerSolver
import app.solver.populationSolver.PopulationCrossSolver
import app.solver.populationSolver.PopulationSecondFloorSolver
import app.solver.Solver
import app.solver.ThistlethwaiteSolver.ThistlethwaiteSolver
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

    private val cubeInitializationService : CubeInitializationService by inject()
    private val cubeMotionService : CubeMotionService by inject()
    private val consoleUI : ConsoleUI by inject()
    private val orientationService : OrientationService by inject()
    private val robotService : RobotOtvintaService by inject()
    private val logger = LoggerFactory.getLogger(Launcher::class.java)

    val useRobot = false
    val displayCube = false
    val displaySolutions = false

    fun launch() {

        val myOrientation = orientationService.getOrientation(Pair(RelativePosition.TOP, Color.WHITE), Pair(
            RelativePosition.FRONT, Color.GREEN))

        var cube = Cube(3, myOrientation)

        if(useRobot) robotService.init(myOrientation)

        for(i in 0..3)
        {
            //cubeInitializationService.initCubeByKeyboard(cube)
            //cubeInitializationService.initCubeWithRobot(cube)
            //cubeInitializationService.initSolvedCube(cube)
            cubeInitializationService.initScrambledCube(cube, 500, false)

            if (displayCube) consoleUI.displayCube(cube)

            var solution: Array<Movement>
            var totalSolution = arrayOf<Movement>()

            var solvers = arrayOf<Solver>(
                //ThistlethwaiteSolver()
                PopulationCrossSolver(10000, 0.001f, 9, true),
                PopulationCornerSolver(10000, 0.001f, 9, true),
                PopulationSecondFloorSolver(10000, 0.001f, 9, true),
                ExhaustiveOLLSolver(cube),
                ExhaustivePLLSolver(cube)
            )

            for (solver in solvers) {
                logger.info("Using " + solver.toString() + " on cube " + cube.toString())

                try {
                    solution = solver.solve(cube)!!

                    cubeMotionService.applySequence(cube, solution)

                    totalSolution = totalSolution.plus(solution)

                    if (displaySolutions) consoleUI.displaySequence(solution)

                    if (displayCube) consoleUI.displayCube(cube)
                }
                catch (e : Exception)
                {
                    println(solver)
                    consoleUI.displayCube(cube)
                }
            }

            logger.info("Total length of solution : " + totalSolution.size)

            if(useRobot) robotService.applySequence(totalSolution)

        }

    }

}
