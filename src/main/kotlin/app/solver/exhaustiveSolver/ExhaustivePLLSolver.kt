package app.solver.exhaustiveSolver

import app.*
import app.model.Color
import app.model.cube.Cube
import app.model.movement.*
import app.model.orientation.RelativePosition
import app.service.orientation.OrientationService
import app.service.cube.CubeInformationService
import org.koin.core.inject

class ExhaustivePLLSolver(cube: Cube) : ExhaustiveSolver(cube) {

    override var listOfSequences = setOf<Array<Movement>>()

    val cubeInformationService : CubeInformationService by inject()

    // Define search space
    init {
        var orientationService = OrientationService()
        for(i in 0 until 4)
        {
            for(relativeSequence in arrayOf(
                PLL_ONE,
                PLL_TWO,
                PLL_THREE,
                PLL_FOUR,
                PLL_FIVE,
                PLL_SIX,
                PLL_SEVEN,
                PLL_EIGHT,
                PLL_NINE,
                PLL_TEN,
                PLL_ELEVEN,
                PLL_TWELVE,
                PLL_THIRTEEN,
                PLL_FOURTEEN,
                PLL_FIFTEEN,
                PLL_SIXTEEN,
                PLL_SEVENTEEN,
                PLL_EIGHTTEEN,
                PLL_NINETEEN,
                PLL_TWENTY,
                PLL_TWENTYONE
            ))
            {
                for(orientation in orientationService.getOrientations(Pair(RelativePosition.TOP, Color.YELLOW)))
                {
                    var toAdd = arrayOf<Movement>()
                    when(i)
                    {
                        0 -> toAdd = movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation)
                        1 -> toAdd = arrayOf(Movement.YELLOW).plus(movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation))
                        2 -> toAdd = arrayOf(Movement.YELLOW_DOUBLE).plus(movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation))
                        3 -> toAdd = arrayOf(Movement.YELLOW_REVERSE).plus(movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation))
                    }
                    listOfSequences = listOfSequences.plus(arrayOf(toAdd))
                }
            }
            listOfSequences += Movement.values().filter { it.name.startsWith("YELLOW") }.map { arrayOf(it) }
        }
    }

    // Goal
    override fun isSolved(cube: Cube): Boolean {
        return cubeInformationService.getNumberOfPiecesOfAColorSolved(cube, Color.YELLOW) == 9
    }
}