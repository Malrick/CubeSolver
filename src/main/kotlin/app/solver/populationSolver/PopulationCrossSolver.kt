package app.solver.populationSolver

import app.model.cube.Cube
import app.model.Color
import app.model.movement.Movement

class PopulationCrossSolver(
    override val populationMaxSize: Int,
    override val ratioOfSurvivingPopulation: Float,
    override val maxNumberOfMutationsAdded: Int,
    override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    // TODO Modifier l'heuristique

    override var listOfMovements = Movement.values().map { arrayOf(it) }.toSet()
    override var maxScore = 400

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score : Int
        /*
var scoreA = 0
var scoreB = 0
var scoreC = 0
var scoreD = 0

var numberOfWhiteEdges = cubeService.getNumberOfEdgeOfAColorBySide(cubeExperiment, "White", "White")
var numberOfSolvedEdges = cubeService.getNumberOfSolvedEdgesBySide(cubeExperiment, "White")

if(numberOfSolvedEdges==4) // Cross solved
{
    solved = true
}
else if(numberOfWhiteEdges==0) // Cross not started
{
}
else // Cross started
{
    // If all the white edges are solved
    if(numberOfSolvedEdges == numberOfWhiteEdges)
    {
    }
    else // If all the white edges are not solved
    {
        // Maybe there is only one edge on the white side ?
        if(numberOfWhiteEdges == 1)
        {
        }
        else // If there is more than one edge on the white side
        {
            // Finding edges well placed one another
            var edgesWellPlacedRelatively = findEdgesWellPlacedRelatively()

            var numberOfEdgeWellPlacedRelatively = edgesWellPlacedRelatively.size

            // If the cross is fully well arranged, but not well placed
            if(numberOfEdgeWellPlacedRelatively == 4)
            {
                // Let's get it to it's right position
                listOfMovements = arrayOf('W')
            }
            // If edges are all well placed relatively, but the cross is not full
            else if(numberOfEdgeWellPlacedRelatively == numberOfWhiteEdges)
            {   // It's a good cross !! Just badly placed
            }
            else
            {
                // Some edges are not well placed relatively

                //  rewarding the edges well placed relatively
                //  Giving some rewards to the one which are not well placed

            }

        }

    }

}
*/

        score = cubeInformationService.getNumberOfEdgesSolvedBySide(cubeExperiment, Color.WHITE) * 100

        // BLUE
        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.BLUE).elementAt(0).possessColor(
                Color.BLUE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.BLUE).elementAt(0).possessColor(
                Color.ORANGE)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.ORANGE)[4] == Color.WHITE) score = score + 50

        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.BLUE).elementAt(0).possessColor(
                Color.BLUE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.BLUE).elementAt(0).possessColor(
                Color.RED)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.RED)[6] == Color.WHITE) score = score + 50

        // RED
        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.RED).elementAt(0).possessColor(
                Color.RED)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.RED).elementAt(0).possessColor(
                Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.GREEN)[6] == Color.WHITE) score = score + 50

        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.RED).elementAt(0).possessColor(
                Color.BLUE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.RED).elementAt(0).possessColor(
                Color.RED)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.RED)[6] == Color.RED) score = score + 50

        // GREEN
        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.GREEN).elementAt(0).possessColor(
                Color.RED)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.GREEN).elementAt(0).possessColor(
                Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.GREEN)[6] == Color.GREEN) score = score + 50

        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.GREEN).elementAt(0).possessColor(
                Color.ORANGE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.GREEN).elementAt(0).possessColor(
                Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.GREEN)[4] == Color.GREEN) score = score + 50

        // ORANGE
        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.ORANGE).elementAt(0).possessColor(
                Color.ORANGE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.ORANGE).elementAt(0).possessColor(
                Color.GREEN)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.ORANGE)[6] == Color.ORANGE) score = score + 50

        if(cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.ORANGE).elementAt(0).possessColor(
                Color.ORANGE)
            && cubeInformationService.getPositionsOfEdges(cubeExperiment, Color.WHITE, Color.ORANGE).elementAt(0).possessColor(
                Color.BLUE)
            && cubeInformationService.getSideByColor(cubeExperiment, Color.ORANGE)[4] == Color.ORANGE) score = score + 50

        if(score == 400)
        {
            solved = true
        }

        return score
    }


}