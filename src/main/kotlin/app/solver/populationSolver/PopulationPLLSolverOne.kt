package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Movement
import app.model.constants.PLL_ONE
import app.model.constants.PLL_ONE_SECOND
import app.service.EdgeService
import org.koin.core.inject

class PopulationPLLSolverOne : PopulationSolver() {

    val edgeService : EdgeService by inject()

    override var listOfMovements: Set<Array<Movement>> = setOf(PLL_ONE, PLL_ONE_SECOND, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score = 0

        if(edgeService.findEdgeByName(cubeExperiment.edges,'I')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'J')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'K')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'L')!!.isSolved()) score += 100

        if (score == 400) {
            solved = true
        }

        return score

    }

}