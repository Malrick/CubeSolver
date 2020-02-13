package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Movement
import app.model.constants.PLL_TWO
import app.model.constants.PLL_TWO_SECOND
import app.service.CornerService
import app.service.EdgeService
import org.koin.core.inject

class PopulationPLLSolverTwo(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    val edgeService : EdgeService by inject()
    val cornerService : CornerService by inject()

    override var listOfMovements: Set<Array<Movement>> = setOf(PLL_TWO, PLL_TWO_SECOND, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score = 0

        if(cornerService.findCornerByName(cubeExperiment.corners,'E')!!.isSolved()) score += 50
        if(cornerService.findCornerByName(cubeExperiment.corners,'F')!!.isSolved()) score += 50
        if(cornerService.findCornerByName(cubeExperiment.corners,'G')!!.isSolved()) score += 50
        if(cornerService.findCornerByName(cubeExperiment.corners,'H')!!.isSolved()) score += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'I')!!.isSolved()) score += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'J')!!.isSolved()) score += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'K')!!.isSolved()) score += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'L')!!.isSolved()) score += 50

        if (score == maxScore) {
            solved = true
        }

        return score

    }
}