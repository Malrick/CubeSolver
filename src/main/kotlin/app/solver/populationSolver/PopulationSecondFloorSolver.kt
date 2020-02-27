package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.*

class PopulationSecondFloorSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements= setOf(
        EDGE_INSERTION_1,
        EDGE_INSERTION_2,
        EDGE_INSERTION_3,
        EDGE_INSERTION_4,
        EDGE_INSERTION_5,
        EDGE_INSERTION_6,
        EDGE_INSERTION_7,
        EDGE_INSERTION_8, arrayOf(
        Movement.YELLOW), arrayOf(Movement.YELLOW_REVERSE))
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score = 0

        var numberOfEdgesSolved = (cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.BLUE) -1) + (cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.GREEN) -1) - cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.YELLOW)

        score = numberOfEdgesSolved*100

        if(score == maxScore)
        {
            solved = true
        }

        return score

    }

}