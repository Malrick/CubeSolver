package app.helper

import app.model.cube.Cube
import app.model.cubeOLD.Corner
import app.model.cubeOLD.Edge
import app.model.cubeOLD.Side
import app.vision.ColorResolver
import app.model.cubeUtils.Color
import app.model.cubeOLD.constants.CornerPosition
import app.model.cubeOLD.constants.EdgePosition
import app.model.robot.constants.PositionOfServo
import app.model.robot.constants.PositionOnRobot
import app.service.cube.CubeInformationService
import app.service.cubeOLD.CornerService
import app.service.cubeOLD.CubeService
import app.service.cubeOLD.EdgeService
import app.service.cubeOLD.SideService
import app.service.robot.MotionService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.ArrayList

class InitHelper : KoinComponent {

    val input = Scanner(System.`in`)

    // Todo Creuser bimap
    var cornerIdentityToColor :  HashMap<CornerPosition, Set<Color>> = HashMap()
    var cornerColorToIdentity : HashMap<Set<Color>, CornerPosition> = HashMap()
    var cornerIdentityToCoordinates : HashMap<CornerPosition, Array<Int>> = HashMap()

    var colorResolver = ColorResolver()

    var edgeIdentityToColor :  HashMap<EdgePosition, Set<Color>> = HashMap()
    var edgeColorToIdentity : HashMap<Set<Color>, EdgePosition> = HashMap()
    var edgeIdentityToCoordinates : HashMap<EdgePosition, Array<Int>> = HashMap()

    val cubeInformationService : CubeInformationService by inject()
    val motionService : MotionService by inject()
    val sideService : SideService by inject()
    val edgeService : EdgeService by inject()
    val cornerService : CornerService by inject()


    fun initCube(cube : Cube)
    {
        for(color in Color.values())
        {
            /*when(color)
            {
                Color.WHITE ->
                {
                    motionService.turnCube(PositionOfCubeEnum.TOP)
                }
                Color.ORANGE -> {
                    motionService.turnCube(PositionOfCubeEnum.BOTTOM)
                    motionService.turnCube(PositionOfCubeEnum.RIGHT)
                }
                Color.GREEN -> motionService.turnCube(PositionOfCubeEnum.LEFT)
                Color.RED -> motionService.turnCube(PositionOfCubeEnum.LEFT)
                Color.YELLOW -> {
                    motionService.turnCube(PositionOfCubeEnum.RIGHT)
                    motionService.turnCube(PositionOfCubeEnum.BOTTOM)
                }
                Color.BLUE-> motionService.turnCube(PositionOfCubeEnum.BOTTOM)
            }*/

            var toAdd = ArrayList<Color>()
            toAdd = initSide(color)
            cubeInformationService.initSideByColor(cube, color, toAdd)
        }
    }

    // TODO Re-propriser
    fun initSide(sideColor : Color) : ArrayList<Color>
    {
        var toAdd = ArrayList<Color>()
        println("Please enter the colors of the $sideColor side")

        for(i in 0..2)
        {
            var array = ArrayList<Color>()
            for(j in 0..2)
            {
                if(i==1 && j ==1)
                {
                    array.add(sideColor)
                }
                else
                {
                    when(input.next()[0])
                    {
                        'W' -> array.add(Color.WHITE)
                        'O' -> array.add(Color.ORANGE)
                        'G' -> array.add(Color.GREEN)
                        'R' -> array.add(Color.RED)
                        'Y' -> array.add(Color.YELLOW)
                        'B' -> array.add(Color.BLUE)
                    }
                }
            }
            toAdd.addAll(array)
        }
        return toAdd
    }

    fun takePicsAndResolveColors(color : Color) : ArrayList<Color>
    {
        var result = ArrayList<Color>()
        var temp = ArrayList<Color>()
        var temp2 = ArrayList<Color>()

        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.OUTSIDE, false)
        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.OUTSIDE, true)

        temp.addAll(colorResolver.resolveColors("temp.jpg", true))

        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_TOP]!!, PositionOfServo.INSIDE, false)
        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_BOTTOM]!!, PositionOfServo.INSIDE, true)
        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.OUTSIDE, false)
        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.OUTSIDE, true)

        temp2.addAll(colorResolver.resolveColors("temp2.jpg", true))

        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_RIGHT]!!, PositionOfServo.INSIDE, false)
        motionService.executeCommand(motionService.servos[PositionOnRobot.ARM_LEFT]!!, PositionOfServo.INSIDE, true)


        for(i in 0..8)
        {
            if(i == 3 || i == 5)
            {
                result.add(temp2[i])
            }
            else
            {
                result.add(temp[i])
            }
        }

        if(result[4]!=color)
        {
            println("Mauvais centre")
        }

        println("Final face $color :")
        for(elem in result)
        {
            println(elem)
        }


        return result
    }
/*
    fun initSide(side : Side, sideColor : Color)
    {
        side.sideColor = sideColor
        var colors = takePicsAndResolveColors(sideColor)
        println(sideColor)

        for(i in 0..2)
        {
            var array = arrayOf<Color>()
            for(j in 0..2)
            {
                    array+=colors[i*3+j]
            }
            side.colors+=array
        }
        colors.clear()

    }*/
    fun initCorner(sides : HashMap<Color, Side>) : Array<Corner>
    {
        var corners = arrayOf<Corner>()

        initMaps()

        var solved : Boolean
        var colorOne : Color
        var colorTwo : Color
        var colorThree : Color

        var mapKey : Set<Color>

        var cornerName  = CornerPosition.A

        for(c in CornerPosition.values())
        {
            solved = true

            colorOne = sideService.getColor(sides.get(cornerIdentityToColor.get(c)!!.elementAt(0))!!, cornerIdentityToCoordinates.get(c)!![0], cornerIdentityToCoordinates.get(c)!![1])
            colorTwo = sideService.getColor(sides.get(cornerIdentityToColor.get(c)!!.elementAt(1))!!, cornerIdentityToCoordinates.get(c)!![2],cornerIdentityToCoordinates.get(c)!![3])
            colorThree = sideService.getColor(sides.get(cornerIdentityToColor.get(c)!!.elementAt(2))!!, cornerIdentityToCoordinates.get(c)!![4],cornerIdentityToCoordinates.get(c)!![5])
            mapKey = setOf(colorOne, colorTwo, colorThree)

            try
            {
                cornerName = cornerColorToIdentity[mapKey]!!
            }
            catch (e : KotlinNullPointerException)
            {
                println()
                println("Coin anormal détecté")
                println("Position : $c")
                println("Couleurs :")
                println(colorOne)
                println(colorTwo)
                println(colorThree)
            }

            // Si le nom du coin n'est pas égal au code de sa position
            if(cornerName != c)
            {
                solved = false
            }
            else
            {
                // Si la couleur de la première face vérifiée n'est pas égale à la première couleur repérée
                if(!colorOne.equals(cornerIdentityToColor.get(c)!!.elementAt(0)))
                {
                    solved = false
                }
            }

            corners += Corner(cornerName, c, solved, colorOne, colorTwo, colorThree)
        }

        return corners
    }

    // Todo Trouver un système de vérification d'unicité
    fun initEdge(sides : HashMap<Color, Side>) : Array<Edge>
    {
        var edges = arrayOf<Edge>()

        initMaps()

        var solved : Boolean

        var colorOne : Color
        var colorTwo : Color

        var mapKey : Set<Color>

        var edgeIdentity = EdgePosition.A

        for(position in EdgePosition.values())
        {
            solved = true

            colorOne = sideService.getColor(sides.get(edgeIdentityToColor.get(position)!!.elementAt(0))!!, edgeIdentityToCoordinates.get(position)!![0], edgeIdentityToCoordinates.get(position)!![1])
            colorTwo = sideService.getColor(sides.get(edgeIdentityToColor.get(position)!!.elementAt(1))!!, edgeIdentityToCoordinates.get(position)!![2],edgeIdentityToCoordinates.get(position)!![3])
            mapKey = setOf(colorOne, colorTwo)

            try
            {
                edgeIdentity = edgeColorToIdentity[mapKey]!!
            }
            catch (e : KotlinNullPointerException)
            {
                println()
                println("Arête anormale détectée")
                println("Position : $position")
                println("Couleurs :")
                println(colorOne)
                println(colorTwo)
            }

            // Si le nom de l'arête n'est pas égal au code de sa position
            if(edgeIdentity != position)
            {
                solved = false
            }
            else
            {
                // Si la couleur de la première face vérifiée n'est pas égale à la première couleur repérée
                if(edgeIdentityToColor.get(position)!!.elementAt(0) != (colorOne))
                {
                    solved = false
                }
            }

            var toAdd = Edge(edgeIdentity, colorOne, colorTwo)
            toAdd.setPosition(position)
            toAdd.setSolved(solved)
            edges += toAdd
        }

        return edges
    }

    fun initMaps()
    {
        cornerColorToIdentity = HashMap()
        cornerIdentityToColor = HashMap()
        cornerIdentityToCoordinates = HashMap()

        // On va de la première face à la dernière pour entrer les couleurs
        cornerColorToIdentity[setOf(Color.BLUE, Color.ORANGE, Color.WHITE)] = CornerPosition.A
        cornerColorToIdentity[setOf(Color.BLUE, Color.RED, Color.WHITE)] = CornerPosition.B
        cornerColorToIdentity[setOf(Color.GREEN, Color.ORANGE, Color.WHITE)] = CornerPosition.C
        cornerColorToIdentity[setOf(Color.GREEN, Color.RED, Color.WHITE)] = CornerPosition.D
        cornerColorToIdentity[setOf(Color.BLUE, Color.ORANGE, Color.YELLOW)] = CornerPosition.E
        cornerColorToIdentity[setOf(Color.BLUE, Color.RED, Color.YELLOW)] = CornerPosition.F
        cornerColorToIdentity[setOf(Color.GREEN, Color.ORANGE, Color.YELLOW)] = CornerPosition.G
        cornerColorToIdentity[setOf(Color.GREEN, Color.RED, Color.YELLOW)] = CornerPosition.H


        cornerIdentityToColor[CornerPosition.A] = setOf(Color.BLUE, Color.ORANGE, Color.WHITE)
        cornerIdentityToColor[CornerPosition.B] = setOf(Color.BLUE, Color.RED, Color.WHITE)
        cornerIdentityToColor[CornerPosition.C] = setOf(Color.GREEN, Color.ORANGE, Color.WHITE)
        cornerIdentityToColor[CornerPosition.D] = setOf(Color.GREEN, Color.RED, Color.WHITE)
        cornerIdentityToColor[CornerPosition.E] = setOf(Color.BLUE, Color.ORANGE, Color.YELLOW)
        cornerIdentityToColor[CornerPosition.F] = setOf(Color.BLUE, Color.RED, Color.YELLOW)
        cornerIdentityToColor[CornerPosition.G] = setOf(Color.GREEN, Color.ORANGE, Color.YELLOW)
        cornerIdentityToColor[CornerPosition.H] = setOf(Color.GREEN, Color.RED, Color.YELLOW)

        // Les nombres correspondent aux coordonnées des couleurs des angles sur les faces
        // Les faces sont dans l'ordre alphabétique, c'est à dire telles que recensées dans la map ci-dessus
        // Ligne, colonne, ligne, colonne, ligne, colonne
        cornerIdentityToCoordinates[CornerPosition.A] = arrayOf(2, 0, 0, 0, 0, 0)
        cornerIdentityToCoordinates[CornerPosition.B] = arrayOf(2, 2, 0, 2, 0, 2)
        cornerIdentityToCoordinates[CornerPosition.C] = arrayOf(0, 0, 0, 2, 2, 0)
        cornerIdentityToCoordinates[CornerPosition.D] = arrayOf(0, 2, 0, 0, 2, 2)
        cornerIdentityToCoordinates[CornerPosition.E] = arrayOf(0, 0, 2, 0, 2, 0)
        cornerIdentityToCoordinates[CornerPosition.F] = arrayOf(0, 2, 2, 2, 2, 2)
        cornerIdentityToCoordinates[CornerPosition.G] = arrayOf(2, 0, 2, 2, 0, 0)
        cornerIdentityToCoordinates[CornerPosition.H] = arrayOf(2, 2, 2, 0, 0, 2)

        edgeColorToIdentity = HashMap()
        edgeIdentityToColor = HashMap()
        edgeIdentityToCoordinates = HashMap()

        // On va de la première face à la dernière pour entrer les couleurs
        edgeColorToIdentity[setOf(Color.BLUE, Color.WHITE)] = EdgePosition.A
        edgeColorToIdentity[setOf(Color.ORANGE, Color.WHITE)] = EdgePosition.B
        edgeColorToIdentity[setOf(Color.RED, Color.WHITE)] = EdgePosition.C
        edgeColorToIdentity[setOf(Color.GREEN, Color.WHITE)] = EdgePosition.D
        edgeColorToIdentity[setOf(Color.BLUE, Color.ORANGE)] = EdgePosition.E
        edgeColorToIdentity[setOf(Color.BLUE, Color.RED)] = EdgePosition.F
        edgeColorToIdentity[setOf(Color.GREEN, Color.ORANGE)] = EdgePosition.G
        edgeColorToIdentity[setOf(Color.GREEN, Color.RED)] = EdgePosition.H
        edgeColorToIdentity[setOf(Color.BLUE, Color.YELLOW)] = EdgePosition.I
        edgeColorToIdentity[setOf(Color.ORANGE, Color.YELLOW)] = EdgePosition.J
        edgeColorToIdentity[setOf(Color.RED, Color.YELLOW)] = EdgePosition.K
        edgeColorToIdentity[setOf(Color.GREEN, Color.YELLOW)] = EdgePosition.L

        edgeIdentityToColor[EdgePosition.A] = setOf(Color.BLUE, Color.WHITE)
        edgeIdentityToColor[EdgePosition.B] = setOf(Color.ORANGE, Color.WHITE)
        edgeIdentityToColor[EdgePosition.C] = setOf(Color.RED, Color.WHITE)
        edgeIdentityToColor[EdgePosition.D] = setOf(Color.GREEN, Color.WHITE)
        edgeIdentityToColor[EdgePosition.E] = setOf(Color.BLUE, Color.ORANGE)
        edgeIdentityToColor[EdgePosition.F] = setOf(Color.BLUE, Color.RED)
        edgeIdentityToColor[EdgePosition.G] = setOf(Color.GREEN, Color.ORANGE)
        edgeIdentityToColor[EdgePosition.H] = setOf(Color.GREEN, Color.RED)
        edgeIdentityToColor[EdgePosition.I] = setOf(Color.BLUE, Color.YELLOW)
        edgeIdentityToColor[EdgePosition.J] = setOf(Color.ORANGE, Color.YELLOW)
        edgeIdentityToColor[EdgePosition.K] = setOf(Color.RED, Color.YELLOW)
        edgeIdentityToColor[EdgePosition.L] = setOf(Color.GREEN, Color.YELLOW)

        // Les nombres correspondent aux coordonnées des couleurs des angles sur les faces
        // Les faces sont dans l'ordre alphabétique, c'est à dire telles que recensées dans la map ci-dessus
        // Ligne, colonne, ligne, colonne
        edgeIdentityToCoordinates[EdgePosition.A] = arrayOf(2, 1, 0, 1)
        edgeIdentityToCoordinates[EdgePosition.B] = arrayOf(0, 1, 1, 0)
        edgeIdentityToCoordinates[EdgePosition.C] = arrayOf(0, 1, 1, 2)
        edgeIdentityToCoordinates[EdgePosition.D] = arrayOf(0, 1, 2, 1)
        edgeIdentityToCoordinates[EdgePosition.E] = arrayOf(1, 0, 1, 0)
        edgeIdentityToCoordinates[EdgePosition.F] = arrayOf(1, 2, 1, 2)
        edgeIdentityToCoordinates[EdgePosition.G] = arrayOf(1, 0, 1, 2)
        edgeIdentityToCoordinates[EdgePosition.H] = arrayOf(1, 2, 1, 0)
        edgeIdentityToCoordinates[EdgePosition.I] = arrayOf(0, 1, 2, 1)
        edgeIdentityToCoordinates[EdgePosition.J] = arrayOf(2, 1, 1, 0)
        edgeIdentityToCoordinates[EdgePosition.K] = arrayOf(2, 1, 1, 2)
        edgeIdentityToCoordinates[EdgePosition.L] = arrayOf(2, 1, 0, 1)
    }



}