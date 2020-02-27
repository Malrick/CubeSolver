package app

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.model.cube.Cube
import app.model.cubeUtils.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.robot.SequenceService
import app.solver.populationSolver.*
import app.vision.ColorResolver
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.opencv.core.*
import java.util.*

fun main()
{
    startKoin{
        modules(listOf(serviceModule, helperModule, displayModule, solverModule, visionModule, servoModule))
    }

    System.loadLibrary( Core.NATIVE_LIBRARY_NAME)
    Launcher().launch()
}

class Launcher : KoinComponent {

    //TODO Exceptions

    //TODO OLL / PLL management

    //TODO Multithreading
    //TODO lambdas

    //TODO Robot ENUM / fails
    //TODO Refacto motion
    //TODO Refacto vision

    val cube = Cube(3)
    val cubeMotionService : CubeMotionService by inject()
    val cubeInformationService : CubeInformationService by inject()
    val populationCrossSolver : PopulationCrossSolver by inject()
    val populationCornerSolver : PopulationCornerSolver by inject()
    val populationSecondFloorSolver : PopulationSecondFloorSolver by inject()
    val populationOLLSolverOne : PopulationOLLSolverOne by inject()
    val populationOLLSolverTwo : PopulationOLLSolverTwo by inject()
    val populationPLLSolverOne : PopulationPLLSolverOne by inject()
    val populationPLLSolverTwo : PopulationPLLSolverTwo by inject()
    val consoleUI : ConsoleUI by inject()
    val initHelper : InitHelper by inject()
    //val motionService : SequenceService by inject()
    val colorResolver : ColorResolver by inject()

    fun launch() {



        var finalSolution = arrayOf<Movement>()

        var solution : Array<Movement>

        //motionService.init()

        //motionService.welcome()

        initHelper.initCube(cube)

        consoleUI.display(cube)
        //motionService.release()

        println()
        println("Cross : ")
        solution =  populationCrossSolver.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)

        finalSolution = finalSolution.plus(solution)

        println()
        println("Corners : ")
        solution = populationCornerSolver.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)

      println()
        println("SecondFloor : ")
        solution = populationSecondFloorSolver.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("OLL one : ")
        solution = populationOLLSolverOne.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("OLL two : ")
        solution = populationOLLSolverTwo.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("PLL one : ")
        solution = populationPLLSolverOne.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("PLL two : ")
        solution = populationPLLSolverTwo.solve(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        cubeMotionService.applySequence(cube, solution)
        consoleUI.display(cube)
        finalSolution = finalSolution.plus(solution)


        //motionService.applySequence(finalSolution)

    }
}
