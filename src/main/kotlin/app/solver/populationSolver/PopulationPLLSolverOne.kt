package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.Movement
import app.model.cubeUtils.PLL_ONE
import app.model.cubeUtils.PLL_ONE_SECOND
import app.model.cubeUtils.Color
import app.service.cubeOLD.EdgeService
import org.koin.core.inject

class PopulationPLLSolverOne(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    val edgeService : EdgeService by inject()

    override var listOfMovements: Set<Array<Movement>> = setOf(
        PLL_ONE, PLL_ONE_SECOND, arrayOf(
        Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score = cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.YELLOW) *100

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}