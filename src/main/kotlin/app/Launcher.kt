package app

import app.UI.DisplayHelper
import app.helper.InitHelper
import app.helper.InputHelper
import app.model.Cube
import app.model.constants.Movement
import app.robotLink.MotionService
import app.solver.populationSolver.*
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.opencv.core.*


fun main()
{
    startKoin{
        modules(listOf(serviceModule, helperModule, displayModule, solverModule, inputModule, servoModule))
    }

    System.loadLibrary( Core.NATIVE_LIBRARY_NAME)
    Launcher().launch()
}

class Launcher : KoinComponent {

    val cube = Cube()
    val inputHelper : InputHelper by inject()
    val populationCrossSolver : PopulationCrossSolver by inject()
    val populationCornerSolver : PopulationCornerSolver by inject()
    val populationSecondFloorSolver : PopulationSecondFloorSolver by inject()
    val populationOLLSolverOne : PopulationOLLSolverOne by inject()
    val populationOLLSolverTwo : PopulationOLLSolverTwo by inject()
    val populationPLLSolverOne : PopulationPLLSolverOne by inject()
    val populationPLLSolverTwo : PopulationPLLSolverTwo by inject()
    val populationCubeSolver : PopulationCubeSolver by inject()
    val displayHelper : DisplayHelper by inject()
    val initHelper : InitHelper by inject()
    val motionService : MotionService by inject()

    fun launch() {

        var finalSolution = arrayOf<Movement>()

        var solution : Array<Movement>

        motionService.init()
        motionService.release()

        initHelper.initCube(cube)


        displayHelper.display(cube)

        println()
        println("Cross : ")
        solution =  populationCrossSolver.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("Corners : ")
        solution = populationCornerSolver.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

      println()
        println("SecondFloor : ")
        solution = populationSecondFloorSolver.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("OLL one : ")
        solution = populationOLLSolverOne.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("OLL two : ")
        solution = populationOLLSolverTwo.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("PLL one : ")
        solution = populationPLLSolverOne.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        println()
        println("PLL two : ")
        solution = populationPLLSolverTwo.getSolution(cube)
        for(movement in solution)
        {
            print(movement)
            print(" ")
        }
        println()
        println()
        inputHelper.applySequence(cube, solution)
        displayHelper.display(cube)
        finalSolution = finalSolution.plus(solution)

        motionService.applySequence(finalSolution)

    }
}
