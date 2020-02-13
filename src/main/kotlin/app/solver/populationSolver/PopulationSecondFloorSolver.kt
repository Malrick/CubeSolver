package app.solver.populationSolver

import app.model.Cube
import app.model.constants.*
import app.service.EdgeService
import org.koin.core.inject

class PopulationSecondFloorSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    val edgeService : EdgeService by inject()

    override var listOfMovements= setOf(EDGE_INSERTION_1, EDGE_INSERTION_2, EDGE_INSERTION_3, EDGE_INSERTION_4, EDGE_INSERTION_5, EDGE_INSERTION_6, EDGE_INSERTION_7, EDGE_INSERTION_8, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score = 0

        if(edgeService.findEdgeByName(cubeExperiment.edges, 'E')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges, 'F')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges, 'G')!!.isSolved()) score += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges, 'H')!!.isSolved()) score += 100


        if(score == maxScore)
        {
            solved = true
        }

        return score

    }

}