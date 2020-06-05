package app

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.movement.Movement
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.robot.RobotSequenceService
import app.solver.bruteforceSolver.BruteforceOLLSolver
import app.solver.Solver
import app.solver.bruteforceSolver.BruteforcePLLSolver
import app.solver.populationSolver.PopulationCornerSolver
import app.solver.populationSolver.PopulationCrossSolver
import app.solver.populationSolver.PopulationSecondFloorSolver
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.opencv.core.Core

fun main()
{
    startKoin{modules(listOf(serviceModule, displayModule, visionModule))}
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    Launcher().launch()
}

class Launcher : KoinComponent {

    //TODO Exceptions
    //TODO Multithreading
    //TODO lambdas

    //TODO Robot ENUM / fails
    //TODO Refacto motion

    val cubeInitializationService : CubeInitializationService by inject()
    val cubeMotionService : CubeMotionService by inject()
    val consoleUI : ConsoleUI by inject()
    val robotMotionService : RobotSequenceService by inject()

    fun launch() {


        val cube = Cube(3)


        var solution : Array<Movement>
        var totalSolution = arrayOf<Movement>()

        robotMotionService.init()

        robotMotionService.welcome()

        //initHelper.initCubeByKeyboard(cube)
        //initHelper.initCubeWithRobot(cube)
        cubeInitializationService.initCubeWithRobot(cube)

        consoleUI.displayCube(cube)

        robotMotionService.release()


        var solvers = arrayOf<Solver>(
            PopulationCrossSolver(10000, 0.001f, 5, true),
            PopulationCornerSolver(10000, 0.001f, 5, true),
            PopulationSecondFloorSolver(10000, 0.001f, 5, true),
            BruteforceOLLSolver(),
            BruteforcePLLSolver()
        )




        for(solver in solvers)
        {
            println()
            println(solver)
            solution = solver.solve(cube)!!
            for(movement in solution)
            {
                print(movement)
                print(" ")
            }
            println()
            println()
            cubeMotionService.applySequence(cube, solution)
            consoleUI.displayCube(cube)
            totalSolution = totalSolution.plus(solution)
        }

        println(totalSolution.size)

        robotMotionService.applySequence(totalSolution)
    }
}
