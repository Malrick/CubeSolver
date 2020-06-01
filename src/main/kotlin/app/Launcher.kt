package app

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.model.cube.Cube
import app.model.cubeUtils.Movement
import app.service.cube.CubeMotionService
import app.service.robot.RobotSequenceService
import app.solver.bruteforceSolver.BruteforceOLLSolver
import app.solver.bruteforceSolver.BruteforcePLLSolver
import app.solver.populationSolver.PopulationCornerSolver
import app.solver.populationSolver.PopulationCrossSolver
import app.solver.populationSolver.PopulationSecondFloorSolver
import app.solver.Solver
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.nd4j.linalg.factory.Nd4j
import org.opencv.core.Core
import java.util.*

fun main()
{
    startKoin{modules(listOf(serviceModule, helperModule, displayModule, visionModule, servoModule))}
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    Launcher().launch()
}

class Launcher : KoinComponent {

    //TODO Exceptions
    //TODO Multithreading
    //TODO lambdas

    //TODO Robot ENUM / fails
    //TODO Refacto motion
    //TODO Refacto vision

    val cubeMotionService : CubeMotionService by inject()
    val consoleUI : ConsoleUI by inject()
    val initHelper : InitHelper by inject()
    val robotMotionService : RobotSequenceService by inject()

    fun launch() {


        val cube = Cube(3)


        var solution : Array<Movement>
        var totalSolution = arrayOf<Movement>()

        robotMotionService.init()

        robotMotionService.welcome()
        //initHelper.initCubeByKeyboard(cube)
        //initHelper.initCubeWithRobot(cube)

        //consoleUI.displayCube(cube)

        var i = 0

        while(true)
        {
            i+=1
            initHelper.processColorsAndSaveLab()
            if(i%5==0)
            {
                println("Appuyez sur une touche pour continuer.")
                Scanner(System.`in`).nextLine()
            }
        }

        consoleUI.displayCube(cube)

        robotMotionService.release()


        /*var solvers = arrayOf<Solver>(
            PopulationCrossSolver(1000, 0.01f, 4, true),
                                      PopulationCornerSolver(1000, 0.01f, 3, true),
                                      PopulationSecondFloorSolver(1000, 0.01f, 3, true),
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

        println(totalSolution.size)*/

        //robotMotionService.applySequence(totalSolution)
    }
}
