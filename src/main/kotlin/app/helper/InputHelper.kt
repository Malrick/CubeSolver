package app.helper

import app.model.Cube
import app.model.constants.Movement
import app.service.CubeService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class InputHelper : KoinComponent {

    val cubeService : CubeService by inject()
    val input = Scanner(System.`in`)

    /*fun inputMovement(cube : Cube)
    {
        println()
        println("Quel mouvement souhaitez vous réaliser ?")
        translationMapper(cube, input.next()[0])
        println()
    }*/

    fun applySequence(cube : Cube, sequence : Array<Movement>)
    {
        for(movement in sequence)
        {
            translationMapper(cube, movement)
        }
    }

    fun translationMapper(cube : Cube, code : Movement)
    {
        when(code)
        {
            Movement.WHITE -> cubeService.translationWhite(cube)
            Movement.ORANGE -> cubeService.translationOrange(cube)
            Movement.GREEN -> cubeService.translationGreen(cube)
            Movement.RED -> cubeService.translationRed(cube)
            Movement.YELLOW -> cubeService.translationYellow(cube)
            Movement.BLUE -> cubeService.translationBlue(cube)
            Movement.WHITE_REVERSE -> cubeService.translationWhitePrime(cube)
            Movement.ORANGE_REVERSE -> cubeService.translationOrangePrime(cube)
            Movement.GREEN_REVERSE -> cubeService.translationGreenPrime(cube)
            Movement.RED_REVERSE -> cubeService.translationRedPrime(cube)
            Movement.YELLOW_REVERSE -> cubeService.translationYellowPrime(cube)
            Movement.BLUE_REVERSE -> cubeService.translationBluePrime(cube)
        }
    }

}