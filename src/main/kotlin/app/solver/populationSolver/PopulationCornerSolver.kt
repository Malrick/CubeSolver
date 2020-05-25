package app.solver.populationSolver

import app.model.cube.Cube
import app.model.cubeUtils.*

class PopulationCornerSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var maxScore = 400

    override var listOfMovements = setOf<Array<Movement>>()

    init{
        var orientationService = OrientationService()
        for(relativeSequence in arrayOf(RELATIVE_CORNER_INSERTION_1, RELATIVE_CORNER_INSERTION_2, RELATIVE_CORNER_INSERTION_3, RELATIVE_CORNER_INSERTION_4))
        {
            for(orientation in orientationService.getOrientations(Pair(RelativePosition.TOP, Color.YELLOW)))
            {
                listOfMovements = listOfMovements.plus(arrayOf(orientationService.convertSequenceOfRelativeMovements(relativeSequence, orientation)))
            }
        }

        listOfMovements = listOfMovements.plus(arrayOf(arrayOf(Movement.YELLOW)))
        listOfMovements = listOfMovements.plus(arrayOf(arrayOf(Movement.YELLOW_REVERSE)))
    }

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score : Int

        score = cubeInformationService.getNumberOfCornersSolvedBySide(cubeExperiment, Color.WHITE) * 100

        if(cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.WHITE) != 4) score = 0

        if(score == 400) solved = true

        return score
    }

}