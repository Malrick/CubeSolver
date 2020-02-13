package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Color
import app.model.constants.Movement
import app.model.constants.OLL_TWO

class PopulationOLLSolverTwo(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements: Set<Array<Movement>> =
        setOf(OLL_TWO, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE), arrayOf(Movement.BLUE), arrayOf(Movement.ORANGE), arrayOf(Movement.GREEN), arrayOf(Movement.RED), arrayOf(Movement.BLUE_REVERSE), arrayOf(Movement.ORANGE_REVERSE), arrayOf(Movement.GREEN_REVERSE), arrayOf(Movement.RED_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score: Int

        score = cubeService.getNumberOfCornerOfAColorBySideColor(cubeExperiment, Color.YELLOW, Color.YELLOW) * 50
        score += cubeService.getNumberOfEdgeOfAColorBySideColor(cubeExperiment, Color.YELLOW, Color.YELLOW) * 50

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}