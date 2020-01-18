package app.solver.populationSolver

import app.model.Cube
import app.model.constants.Color
import app.model.constants.Movement
import app.service.EdgeService
import app.service.SideService
import org.koin.core.inject

class PopulationCrossSolver : PopulationSolver() {

    // TODO Modifier l'heuristique
    val sideService : SideService by inject()
    val edgeService : EdgeService by inject()

    override var listOfMovements = Movement.values().map { arrayOf(it) }.toSet()

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = Cube()

        initHelper.initCubeByCopy(cubeExperiment, cube)

        inputHelper.applySequence(cubeExperiment, sequence)

        var score : Int
        var scoreA = 0
        var scoreB = 0
        var scoreC = 0
        var scoreD = 0
        /*
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

        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.isSolved()) scoreA += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.isSolved()) scoreB += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.isSolved()) scoreC += 100
        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.isSolved()) scoreD += 100

        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.isBadlyOriented()) scoreA += 25
        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.isBadlyOriented()) scoreB += 25
        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.isBadlyOriented()) scoreC += 25
        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.isBadlyOriented()) scoreD += 25

        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.getPosition() == 'E' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.ORANGE), 1, 0) == Color.WHITE) scoreA += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.getPosition() == 'F' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.RED), 1, 2) == Color.WHITE) scoreA += 50

        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.getPosition() == 'E' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.BLUE), 1, 0) == Color.WHITE) scoreB += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.getPosition() == 'G' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.GREEN), 1, 0) == Color.WHITE) scoreB += 50

        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.getPosition() == 'F' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.BLUE), 1, 2) == Color.WHITE) scoreC += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.getPosition() == 'H' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.ORANGE), 1, 2) == Color.WHITE) scoreC += 50

        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.getPosition() == 'G' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.ORANGE), 1, 2) == Color.WHITE) scoreD += 50
        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.getPosition() == 'H' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.RED), 1, 0) == Color.WHITE) scoreD += 50

        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.getPosition() == 'I' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 2, 1) == Color.WHITE) scoreA += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.getPosition() == 'J' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 0) != Color.WHITE) scoreA += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'A')!!.getPosition() == 'K' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 2) != Color.WHITE) scoreA += 10


        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.getPosition() == 'J' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 0) == Color.WHITE) scoreB += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.getPosition() == 'I' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 0, 1) != Color.WHITE) scoreB += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'B')!!.getPosition() == 'L' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 2, 1) != Color.WHITE) scoreB += 10


        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.getPosition() == 'K' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 2) == Color.WHITE) scoreC += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.getPosition() == 'I' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 0, 1) != Color.WHITE) scoreC += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'C')!!.getPosition() == 'L' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 2, 1) != Color.WHITE) scoreC += 10


        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.getPosition() == 'L' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 0, 1) == Color.WHITE) scoreD += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.getPosition() == 'J' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 0) != Color.WHITE) scoreD += 10
        if(edgeService.findEdgeByName(cubeExperiment.edges,'D')!!.getPosition() == 'K' && sideService.getColor(cubeService.getSideByColor(cubeExperiment, Color.YELLOW), 1, 2) != Color.WHITE) scoreD += 10

        score = scoreA + scoreB + scoreC + scoreD

        if(score == 400) solved = true

        return score
    }


}