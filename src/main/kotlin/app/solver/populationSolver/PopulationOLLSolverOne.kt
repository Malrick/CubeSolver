package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.cubeUtils.OLL_ONE

class PopulationOLLSolverOne(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements: Set<Array<Movement>> = setOf(
        OLL_ONE, arrayOf(
        Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score = 0

        var colors = cubeInformationService.getSideByColor(cubeExperiment, Color.YELLOW)

        if(colors[1] == Color.YELLOW) score = score+100
        if(colors[3] == Color.YELLOW) score = score+100
        if(colors[5] == Color.YELLOW) score = score+100
        if(colors[7] == Color.YELLOW) score = score+100

        if (score == maxScore) {
            solved = true
        }

        return score

    }

}