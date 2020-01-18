package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Color
import app.model.constants.Movement
import app.model.constants.OLL_ONE

class PopulationOLLSolverOne : PopulationSolver() {

    override var listOfMovements: Set<Array<Movement>> = setOf(OLL_ONE, arrayOf(Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score: Int

        score = cubeService.getNumberOfEdgeOfAColorBySideColor(cubeExperiment, Color.YELLOW, Color.YELLOW) * 100

        if (score == 400) {
            solved = true
        }

        return score

    }

}