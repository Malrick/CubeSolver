package app.solver.populationSolver

import app.model.Cube
import app.model.constants.*
import app.model.constants.Movement
import app.service.CornerService
import app.service.EdgeService
import org.koin.core.inject

class PopulationCornerSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    val cornerService : CornerService by inject()
    val edgeService : EdgeService by inject()

    override var listOfMovements = setOf(CORNER_INSERSION_1, CORNER_INSERSION_2, CORNER_INSERSION_3, CORNER_INSERSION_4, CORNER_INSERSION_5, CORNER_INSERSION_6, CORNER_INSERSION_7, CORNER_INSERSION_8, CORNER_INSERSION_9, CORNER_INSERSION_10, CORNER_INSERSION_11, CORNER_INSERSION_12, CORNER_INSERSION_13, CORNER_INSERSION_14, CORNER_INSERSION_15, CORNER_INSERSION_16, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score : Int
        var scoreA = 0
        var scoreB = 0
        var scoreC = 0
        var scoreD = 0

        if(cornerService.findCornerByName(cubeExperiment.corners, 'A')!!.isSolved()) scoreA += 100
        if(cornerService.findCornerByName(cubeExperiment.corners, 'B')!!.isSolved()) scoreB += 100
        if(cornerService.findCornerByName(cubeExperiment.corners, 'C')!!.isSolved()) scoreC += 100
        if(cornerService.findCornerByName(cubeExperiment.corners, 'D')!!.isSolved()) scoreD += 100

        score = scoreA + scoreB + scoreC + scoreD

        if(score == 400) solved = true

        return score
    }

}