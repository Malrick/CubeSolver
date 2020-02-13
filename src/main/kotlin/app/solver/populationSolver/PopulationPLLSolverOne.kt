package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Movement
import app.model.constants.PLL_ONE
import app.model.constants.PLL_ONE_SECOND
import app.service.EdgeService
import org.koin.core.inject

class PopulationPLLSolverOne(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    val edgeService : EdgeService by inject()

    override var listOfMovements: Set<Array<Movement>> = setOf(PLL_ONE, PLL_ONE_SECOND, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score = 0

        if(edgeService.findEdgeByName(cubeExperiment.edges,'I')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'J')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'K')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'L')!!.isSolved()) score += 100

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}