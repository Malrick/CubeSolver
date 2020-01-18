package app.helper

import app.model.Corner
import app.model.Cube
import app.model.Edge
import app.model.Side
import app.model.constants.Color
import app.service.CubeService
import app.service.SideService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.Comparator

class InitHelper : KoinComponent {

    val input = Scanner(System.`in`)

    // Todo Creuser bimap
    var cornerNameToColor :  HashMap<Char, Set<Color>> = HashMap()
    var cornerColorToName : HashMap<Set<Color>, Char> = HashMap()
    var cornerNameToCoordinates : HashMap<Char, Array<Int>> = HashMap()

    var edgeNameToColor :  HashMap<Char, Set<Color>> = HashMap()
    var edgeColorToName : HashMap<Set<Color>, Char> = HashMap()
    var edgeNameToCoordinates : HashMap<Char, Array<Int>> = HashMap()

    val sideService : SideService by inject()
    val cubeService : CubeService by inject()

    fun initCube(cube : Cube)
    {
        for(color in Color.values())
        {
            var toAdd = Side()
            initSide(toAdd, color)
            cube.sides.put(color, toAdd)
        }
        cube.corners = initCorner(cube.sides)
        cube.edges = initEdge(cube.sides)
    }

    fun initCubeByCopy(cube : Cube, toCopy : Cube)
    {
        for(color in Color.values())
        {
            var toAdd = Side()
            initSideByCopy(toAdd, color, cubeService.getSideByColor(toCopy, color))
            cube.sides.put(color, toAdd)
        }
        cube.corners = initCorner(cube.sides)
        cube.edges = initEdge(cube.sides)
    }

    fun initSide(side : Side, sideColor : Color)
    {
        side.sideColor = sideColor
        println("Please enter the colors of the $sideColor side")

        for(i in 0..2)
        {
            var array = arrayOf<Color>()
            for(j in 0..2)
            {
                if(i==1 && j ==1)
                {
                    array += sideColor
                }
                else
                {
                    when(input.next()[0])
                    {
                        'W' -> array += Color.WHITE
                        'O' -> array += Color.ORANGE
                        'G' -> array += Color.GREEN
                        'R' -> array += Color.RED
                        'Y' -> array += Color.YELLOW
                        'B' -> array += Color.BLUE
                    }
                }
            }
            side.colors+=array
        }
    }


    fun initSideByCopy(side : Side, sideColor : Color, toCopy : Side)
    {
        side.sideColor = sideColor

        for(i in 0..2)
        {
            var array = arrayOf<Color>()
            for(j in 0..2)
            {
                array+= toCopy.colors[i][j]
            }
            side.colors+=array
        }
    }

    fun initCorner(sides : HashMap<Color, Side>) : Array<Corner>
    {
        var corners = arrayOf<Corner>()

        initMaps()

        var positionList = arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')

        var solved : Boolean;
        var colorOne : Color
        var colorTwo : Color
        var colorThree : Color

        var mapKey : Set<Color>

        var cornerName  = 'A'

        for(c in positionList)
        {
            solved = true

            colorOne = sideService.getColor(sides.get(cornerNameToColor.get(c)!!.elementAt(0))!!, cornerNameToCoordinates.get(c)!![0], cornerNameToCoordinates.get(c)!![1])
            colorTwo = sideService.getColor(sides.get(cornerNameToColor.get(c)!!.elementAt(1))!!, cornerNameToCoordinates.get(c)!![2],cornerNameToCoordinates.get(c)!![3])
            colorThree = sideService.getColor(sides.get(cornerNameToColor.get(c)!!.elementAt(2))!!, cornerNameToCoordinates.get(c)!![4],cornerNameToCoordinates.get(c)!![5])
            mapKey = setOf(colorOne, colorTwo, colorThree)

            try
            {
                cornerName = cornerColorToName[mapKey]!!
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
                if(!colorOne.equals(cornerNameToColor.get(c)!!.elementAt(0)))
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

        var positionList = arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L')

        var solved : Boolean;

        var colorOne : Color
        var colorTwo : Color

        var mapKey : Set<Color>

        var edgeName = 'A'

        for(position in positionList)
        {
            solved = true

            colorOne = sideService.getColor(sides.get(edgeNameToColor.get(position)!!.elementAt(0))!!, edgeNameToCoordinates.get(position)!![0], edgeNameToCoordinates.get(position)!![1])
            colorTwo = sideService.getColor(sides.get(edgeNameToColor.get(position)!!.elementAt(1))!!, edgeNameToCoordinates.get(position)!![2],edgeNameToCoordinates.get(position)!![3])
            mapKey = setOf(colorOne, colorTwo)

            try
            {
                edgeName = edgeColorToName[mapKey]!!
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
            if(edgeName != position)
            {
                solved = false
            }
            else
            {
                // Si la couleur de la première face vérifiée n'est pas égale à la première couleur repérée
                if(edgeNameToColor.get(position)!!.elementAt(0) != (colorOne))
                {
                    solved = false
                }
            }

            edges += Edge(edgeName, position, solved, colorOne, colorTwo)
        }

        return edges
    }

    fun initMaps()
    {
        cornerColorToName = HashMap()
        cornerNameToColor = HashMap()
        cornerNameToCoordinates = HashMap()

        // On va de la première face à la dernière pour entrer les couleurs
        cornerColorToName[setOf(Color.BLUE, Color.ORANGE, Color.WHITE)] = 'A'
        cornerColorToName[setOf(Color.BLUE, Color.RED, Color.WHITE)] = 'B'
        cornerColorToName[setOf(Color.GREEN, Color.ORANGE, Color.WHITE)] = 'C'
        cornerColorToName[setOf(Color.GREEN, Color.RED, Color.WHITE)] = 'D'
        cornerColorToName[setOf(Color.BLUE, Color.ORANGE, Color.YELLOW)] = 'E'
        cornerColorToName[setOf(Color.BLUE, Color.RED, Color.YELLOW)] = 'F'
        cornerColorToName[setOf(Color.GREEN, Color.ORANGE, Color.YELLOW)] = 'G'
        cornerColorToName[setOf(Color.GREEN, Color.RED, Color.YELLOW)] = 'H'


        cornerNameToColor['A'] = setOf(Color.BLUE, Color.ORANGE, Color.WHITE)
        cornerNameToColor['B'] = setOf(Color.BLUE, Color.RED, Color.WHITE)
        cornerNameToColor['C'] = setOf(Color.GREEN, Color.ORANGE, Color.WHITE)
        cornerNameToColor['D'] = setOf(Color.GREEN, Color.RED, Color.WHITE)
        cornerNameToColor['E'] = setOf(Color.BLUE, Color.ORANGE, Color.YELLOW)
        cornerNameToColor['F'] = setOf(Color.BLUE, Color.RED, Color.YELLOW)
        cornerNameToColor['G'] = setOf(Color.GREEN, Color.ORANGE, Color.YELLOW)
        cornerNameToColor['H'] = setOf(Color.GREEN, Color.RED, Color.YELLOW)

        // Les nombres correspondent aux coordonnées des couleurs des angles sur les faces
        // Les faces sont dans l'ordre alphabétique, c'est à dire telles que recensées dans la map ci-dessus
        // Ligne, colonne, ligne, colonne, ligne, colonne
        cornerNameToCoordinates['A'] = arrayOf(2, 0, 0, 0, 0, 0)
        cornerNameToCoordinates['B'] = arrayOf(2, 2, 0, 2, 0, 2)
        cornerNameToCoordinates['C'] = arrayOf(0, 0, 0, 2, 2, 0)
        cornerNameToCoordinates['D'] = arrayOf(0, 2, 0, 0, 2, 2)
        cornerNameToCoordinates['E'] = arrayOf(0, 0, 2, 0, 2, 0)
        cornerNameToCoordinates['F'] = arrayOf(0, 2, 2, 2, 2, 2)
        cornerNameToCoordinates['G'] = arrayOf(2, 0, 2, 2, 0, 0)
        cornerNameToCoordinates['H'] = arrayOf(2, 2, 2, 0, 0, 2)

        edgeColorToName = HashMap()
        edgeNameToColor = HashMap()
        edgeNameToCoordinates = HashMap()

        // On va de la première face à la dernière pour entrer les couleurs
        edgeColorToName[setOf(Color.BLUE, Color.WHITE)] = 'A'
        edgeColorToName[setOf(Color.ORANGE, Color.WHITE)] = 'B'
        edgeColorToName[setOf(Color.RED, Color.WHITE)] = 'C'
        edgeColorToName[setOf(Color.GREEN, Color.WHITE)] = 'D'
        edgeColorToName[setOf(Color.BLUE, Color.ORANGE)] = 'E'
        edgeColorToName[setOf(Color.BLUE, Color.RED)] = 'F'
        edgeColorToName[setOf(Color.GREEN, Color.ORANGE)] = 'G'
        edgeColorToName[setOf(Color.GREEN, Color.RED)] = 'H'
        edgeColorToName[setOf(Color.BLUE, Color.YELLOW)] = 'I'
        edgeColorToName[setOf(Color.ORANGE, Color.YELLOW)] = 'J'
        edgeColorToName[setOf(Color.RED, Color.YELLOW)] = 'K'
        edgeColorToName[setOf(Color.GREEN, Color.YELLOW)] = 'L'

        edgeNameToColor['A'] = setOf(Color.BLUE, Color.WHITE)
        edgeNameToColor['B'] = setOf(Color.ORANGE, Color.WHITE)
        edgeNameToColor['C'] = setOf(Color.RED, Color.WHITE)
        edgeNameToColor['D'] = setOf(Color.GREEN, Color.WHITE)
        edgeNameToColor['E'] = setOf(Color.BLUE, Color.ORANGE)
        edgeNameToColor['F'] = setOf(Color.BLUE, Color.RED)
        edgeNameToColor['G'] = setOf(Color.GREEN, Color.ORANGE)
        edgeNameToColor['H'] = setOf(Color.GREEN, Color.RED)
        edgeNameToColor['I'] = setOf(Color.BLUE, Color.YELLOW)
        edgeNameToColor['J'] = setOf(Color.ORANGE, Color.YELLOW)
        edgeNameToColor['K'] = setOf(Color.RED, Color.YELLOW)
        edgeNameToColor['L'] = setOf(Color.GREEN, Color.YELLOW)

        // Les nombres correspondent aux coordonnées des couleurs des angles sur les faces
        // Les faces sont dans l'ordre alphabétique, c'est à dire telles que recensées dans la map ci-dessus
        // Ligne, colonne, ligne, colonne
        edgeNameToCoordinates['A'] = arrayOf(2, 1, 0, 1)
        edgeNameToCoordinates['B'] = arrayOf(0, 1, 1, 0)
        edgeNameToCoordinates['C'] = arrayOf(0, 1, 1, 2)
        edgeNameToCoordinates['D'] = arrayOf(0, 1, 2, 1)
        edgeNameToCoordinates['E'] = arrayOf(1, 0, 1, 0)
        edgeNameToCoordinates['F'] = arrayOf(1, 2, 1, 2)
        edgeNameToCoordinates['G'] = arrayOf(1, 0, 1, 2)
        edgeNameToCoordinates['H'] = arrayOf(1, 2, 1, 0)
        edgeNameToCoordinates['I'] = arrayOf(0, 1, 2, 1)
        edgeNameToCoordinates['J'] = arrayOf(2, 1, 1, 0)
        edgeNameToCoordinates['K'] = arrayOf(2, 1, 1, 2)
        edgeNameToCoordinates['L'] = arrayOf(2, 1, 0, 1)
    }


}