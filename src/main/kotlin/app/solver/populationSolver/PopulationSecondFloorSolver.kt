package app.solver.populationSolver

import app.model.Color
import app.model.cube.Cube
import app.model.movement.Movement
import app.model.movement.RELATIVE_EDGE_INSERTION_1
import app.model.movement.RELATIVE_EDGE_INSERTION_2
import app.model.movement.RelativePosition
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
    }


    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score : Int

        var numberOfEdgesSolved = (cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.BLUE) -1) + (cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.GREEN) -1) - cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.YELLOW)

        score = numberOfEdgesSolved*100

        if(score == maxScore)
        {
            solved = true
        }

        return score

    }

}