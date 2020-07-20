package app.solver.populationSolver

import app.model.Color
import app.model.cube.Cube
import app.model.movement.Movement
import app.RELATIVE_EDGE_INSERTION_1
import app.RELATIVE_EDGE_INSERTION_2
import app.UI.ConsoleUI
import app.model.orientation.RelativePosition
import app.service.orientation.OrientationService

class PopulationSecondFloorSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements = setOf<Array<Movement>>()

    override var maxScore = 400

    init{
        var orientationService = OrientationService()
        for(relativeSequence in arrayOf(
            RELATIVE_EDGE_INSERTION_1,
            RELATIVE_EDGE_INSERTION_2
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

        var score = 0

        var numberOfEdgesSolved = cubeInformationService.numberOfEEdgesSolved(cubeExperimental)

        if(cubeInformationService.getNumberOfPiecesOfAColorSolved(cubeExperimental, Color.WHITE) != 9)
        {
            ConsoleUI().displayCube(cubeExperimental)
            ConsoleUI().displaySequence(studiedSequence)
        }
        else
        {
            score = numberOfEdgesSolved*100
            score += cubeInformationService.numberOfEdgesInBottomSideWithoutColor(cubeExperimental, Color.YELLOW)
        }

        if(score == maxScore)
        {
            solved = true
        }

        cubeMotionService.applySequence(cubeExperimental, movementService.getOppositeSequence(studiedSequence))

        return score

    }

}