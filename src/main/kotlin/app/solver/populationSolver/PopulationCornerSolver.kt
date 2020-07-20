package app.solver.populationSolver

import app.RELATIVE_CORNER_INSERTION_1
import app.RELATIVE_CORNER_INSERTION_2
import app.RELATIVE_CORNER_INSERTION_3
import app.RELATIVE_CORNER_INSERTION_4
import app.model.Color
import app.model.cube.Cube
import app.model.movement.*
import app.model.orientation.RelativePosition
import app.service.orientation.OrientationService

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
        for(relativeSequence in arrayOf(
            RELATIVE_CORNER_INSERTION_1,
            RELATIVE_CORNER_INSERTION_2,
            RELATIVE_CORNER_INSERTION_3,
            RELATIVE_CORNER_INSERTION_4
        ))
        {
            for(orientation in orientationService.getOrientations(Pair(RelativePosition.TOP, Color.YELLOW)))
            {
                listOfMovements = listOfMovements.plus(arrayOf(movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation)))
            }
        }

        listOfMovements = listOfMovements.plus(arrayOf(arrayOf(Movement.YELLOW)))
        listOfMovements = listOfMovements.plus(arrayOf(arrayOf(Movement.YELLOW_REVERSE)))
        listOfMovements = listOfMovements.plus(arrayOf(arrayOf(Movement.YELLOW_DOUBLE)))
    }

    override fun gradeSequence(cube: Cube, studiedSequence: Array<Movement>): Int {

        cubeMotionService.applySequence(cubeExperimental, studiedSequence)

        var score : Int

        score = cubeInformationService.getNumberOfCornersSolvedBySide(cubeExperimental, Color.WHITE) * 100

        if(cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperimental, Color.WHITE) != 4) score = 0

        if(score == 400) solved = true

        cubeMotionService.applySequence(cubeExperimental, movementService.getOppositeSequence(studiedSequence))

        return score
    }

}