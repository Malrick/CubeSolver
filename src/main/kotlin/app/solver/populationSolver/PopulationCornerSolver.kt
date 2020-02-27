package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.*

class PopulationCornerSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {


    override var listOfMovements = setOf(
        CORNER_INSERSION_1,
        CORNER_INSERSION_2,
        CORNER_INSERSION_3,
        CORNER_INSERSION_4,
        CORNER_INSERSION_5,
        CORNER_INSERSION_6,
        CORNER_INSERSION_7,
        CORNER_INSERSION_8,
        CORNER_INSERSION_9,
        CORNER_INSERSION_10,
        CORNER_INSERSION_11,
        CORNER_INSERSION_12,
        CORNER_INSERSION_13,
        CORNER_INSERSION_14,
        CORNER_INSERSION_15,
        CORNER_INSERSION_16, arrayOf(
        Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score : Int

        score = cubeInformationService.getNumberOfCornersSolvedBySide(cubeExperiment, Color.WHITE) * 100

        if(score == 400) solved = true

        return score
    }

}