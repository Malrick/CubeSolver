package app.solver.populationSolver

import app.UI.DisplayHelper
import app.model.Cube
import app.model.constants.Movement
import org.koin.core.inject

class PopulationCubeSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver(){

    val displayHelper : DisplayHelper by inject()

    override var listOfMovements = Movement.values().map { arrayOf(it) }.toSet()
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score = cubeService.getNumberOfSolved(cubeExperiment)*20

        if(score == maxScore && score > 60)
        {
            displayHelper.display(cubeExperiment)
            println(score)
            println(sequence.size)
            for(movement in sequence)
            {
                print(movement)
                print(" ")
            }
            println()
            println()
        }
        return score

    }
}