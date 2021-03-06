package app.solver.exhaustiveSolver

import app.*
import app.model.Color
import app.model.cube.Cube
import app.model.movement.*
import app.model.orientation.RelativePosition
import app.service.orientation.OrientationService
import app.service.cube.CubeInformationService
import org.koin.core.inject

class ExhaustiveOLLSolver(cube: Cube) : ExhaustiveSolver(cube) {

    override var listOfSequences = setOf<Array<Movement>>()

    val cubeInformationService : CubeInformationService by inject()

    // Define search space
    init {
        var orientationService = OrientationService()
        for(relativeSequence in arrayOf(
            OLL_ONE,
            OLL_TWO,
            OLL_THREE,
            OLL_FOUR,
            OLL_FIVE,
            OLL_SIX,
            OLL_SEVEN,
            OLL_EIGHT,
            OLL_NINE,
            OLL_TEN,
            OLL_ELEVEN,
            OLL_TWELVE,
            OLL_THIRTEEN,
            OLL_FOURTEEN,
            OLL_FIFTEEN,
            OLL_SIXTEEN,
            OLL_SEVENTEEN,
            OLL_EIGHTEEN,
            OLL_NINETEEN,
            OLL_TWENTY,
            OLL_TWENTYONE,
            OLL_TWENTYTWO,
            OLL_TWENTYTHREE,
            OLL_TWENTYFOUR,
            OLL_TWENTYFIVE,
            OLL_TWENTYSIX,
            OLL_TWENTYSEVEN,
            OLL_TWENTYEIGHT,
            OLL_TWENTYNINE,
            OLL_THIRTY,
            OLL_THIRTYONE,
            OLL_THIRTYTWO,
            OLL_THIRTYTHREE,
            OLL_THIRTYFOUR,
            OLL_THIRTYFIVE,
            OLL_THIRTYSIX,
            OLL_THIRTYSEVEN,
            OLL_THIRTYEIGHT,
            OLL_THIRTYNINE,
            OLL_FOURTY,
            OLL_FOURTYONE,
            OLL_FOURTYTWO,
            OLL_FOURTYTHREE,
            OLL_FOURTYFOUR,
            OLL_FOURTYFIVE,
            OLL_FOURTYSIX,
            OLL_FOURTYSEVEN,
            OLL_FOURTYEIGHT,
            OLL_FOURTYNINE,
            OLL_FIFTY,
            OLL_FIFTYONE,
            OLL_FIFTYTWO,
            OLL_FIFTYTHREE,
            OLL_FIFTYFOUR,
            OLL_FIFTYFIVE,
            OLL_FIFTYSIX,
            OLL_FIFTYSEVEN
        ))
        {
            for(orientation in orientationService.getOrientations(Pair(RelativePosition.TOP, Color.YELLOW)))
            {
                listOfSequences = listOfSequences.plus(arrayOf(movementService.convertSequenceOfRelativeMovements(relativeSequence, orientation)))
            }
        }
        listOfSequences += Movement.values().filter { it.name.startsWith("YELLOW") }.map { arrayOf(it) }
    }

    // Goal
    override fun isSolved(cube: Cube): Boolean {
        return cubeInformationService.getNumberOfPiecesOfAColorBySide(cube, Color.YELLOW) == 9
    }
}