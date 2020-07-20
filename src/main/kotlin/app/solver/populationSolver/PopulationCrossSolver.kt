package app.solver.populationSolver

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement

class PopulationCrossSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var listOfMovements = Movement.values().map { arrayOf(it) }.toSet()
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, studiedSequence: Array<Movement>): Int {

        cubeMotionService.applySequence(cubeExperimental, studiedSequence)

        var score : Int
        score = cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperimental, Color.WHITE) * 100

        // BLUE
        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.BLUE).possessColor(Color.BLUE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.BLUE).possessColor(Color.ORANGE)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.ORANGE)[4] == Color.WHITE) score = score + 50

        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.BLUE).possessColor(Color.BLUE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.BLUE).possessColor(Color.RED)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.RED)[6] == Color.WHITE) score = score + 50

        // RED
        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.RED).possessColor(Color.RED)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.RED).possessColor(Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.GREEN)[6] == Color.WHITE) score = score + 50

        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.RED).possessColor(Color.BLUE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.RED).possessColor(Color.RED)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.RED)[6] == Color.RED) score = score + 50

        // GREEN
        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.GREEN).possessColor(Color.RED)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.GREEN).possessColor(Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.GREEN)[6] == Color.GREEN) score = score + 50

        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.GREEN).possessColor(Color.ORANGE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.GREEN).possessColor(Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.GREEN)[4] == Color.GREEN) score = score + 50

        // ORANGE
        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.ORANGE).possessColor(Color.ORANGE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.ORANGE).possessColor(Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.ORANGE)[6] == Color.ORANGE) score = score + 50

        if(cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.ORANGE).possessColor(Color.ORANGE)
            && cubeInformationService.getPositionsOfEdge(cubeExperimental, Color.WHITE, Color.ORANGE).possessColor(Color.BLUE)
            && cubeInformationService.getSideByColor(cubeExperimental, Color.ORANGE)[4] == Color.ORANGE) score = score + 50

        if(score >= 400)
        {
            solved = true
        }

        cubeMotionService.applySequence(cubeExperimental, movementService.getOppositeSequence(studiedSequence))
        return score
    }


}