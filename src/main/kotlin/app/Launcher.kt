package app

import app.helper.DisplayHelper
import app.helper.InitHelper
import app.helper.InputHelper
import app.helper.ProjectionHelper
import app.model.Cube
import app.model.constants.Movement
import app.solver.populationSolver.*
import com.github.ajalt.mordant.TermColors
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject


fun main()
{
    startKoin{
        modules(listOf(serviceModule, helperModule, displayModule, solverModule, inputModule))
    }

    Launcher().launch()
}

class Launcher : KoinComponent{

    val cube = Cube()
    val inputHelper : InputHelper by inject()
    val populationCrossSolver : PopulationCrossSolver by inject()
    val populationCornerSolver : PopulationCornerSolver by inject()
    val populationSecondFloorSolver : PopulationSecondFloorSolver by inject()
    val populationOLLSolverOne : PopulationOLLSolverOne by inject()
    val populationOLLSolverTwo : PopulationOLLSolverTwo by inject()
    val populationPLLSolverOne : PopulationPLLSolverOne by inject()
    val populationPLLSolverTwo : PopulationPLLSolverTwo by inject()
    val displayHelper : DisplayHelper by inject()
    val initHelper : InitHelper by inject()

    fun launch() {
        var solution : Array<Movement>

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

      /*  println()
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
        displayHelper.display(cube)*/

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
    }
}
