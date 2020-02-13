package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Color
import app.model.constants.Movement
import app.model.constants.OLL_ONE

class PopulationOLLSolverOne(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements: Set<Array<Movement>> = setOf(OLL_ONE, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score: Int

        score = cubeService.getNumberOfEdgeOfAColorBySideColor(cubeExperiment, Color.YELLOW, Color.YELLOW) * 100

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}