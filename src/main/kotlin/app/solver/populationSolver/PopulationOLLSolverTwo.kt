package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.cubeUtils.OLL_TWO

class PopulationOLLSolverTwo(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements: Set<Array<Movement>> =
        setOf(
            OLL_TWO, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE), arrayOf(
            Movement.BLUE), arrayOf(Movement.ORANGE), arrayOf(
            Movement.GREEN), arrayOf(Movement.RED), arrayOf(
            Movement.BLUE_REVERSE), arrayOf(Movement.ORANGE_REVERSE), arrayOf(
            Movement.GREEN_REVERSE), arrayOf(Movement.RED_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score = (cubeInformationService.getSideByColor(cubeExperiment, Color.YELLOW).filter { it == Color.YELLOW }.size-1)*50

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}