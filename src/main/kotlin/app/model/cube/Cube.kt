package app.model.cube

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Center
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import app.model.cube.position.CenterPosition
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.cube.position.Position
import com.github.ajalt.mordant.TermColors
import kotlin.properties.Delegates

class Cube {

    var cubeSize : Int by Delegates.notNull()
    var positions = HashMap<Position, Piece>()
    var adjacencyList = HashMap<Position, List<Position>>()

    constructor(cubeSize : Int)
    {
        this.cubeSize = cubeSize

        for(height in 0 until cubeSize)
        {
            for(depht in 0 until cubeSize)
            {
                for(width in 0 until cubeSize)
                {
                    var colorsToAddOnPosition = ArrayList<Color>()
                    if(height == 0) colorsToAddOnPosition.add(Color.YELLOW)
                    if(height == cubeSize-1) colorsToAddOnPosition.add(Color.WHITE)
                    if(depht == 0) colorsToAddOnPosition.add(Color.GREEN)
                    if(depht == cubeSize-1) colorsToAddOnPosition.add(Color.BLUE)
                    if(width == 0) colorsToAddOnPosition.add(Color.ORANGE)
                    if(width == cubeSize-1) colorsToAddOnPosition.add(Color.RED)

                    when(colorsToAddOnPosition.size)
                    {
                        1-> positions[CenterPosition(
                            CubeCoordinates(height, depht, width),
                            colorsToAddOnPosition[0]
                        )] = Center(colorsToAddOnPosition[0])
                        2-> positions[EdgePosition(
                            CubeCoordinates(height, depht, width),
                            colorsToAddOnPosition[0],
                            colorsToAddOnPosition[1]
                        )] = Edge(colorsToAddOnPosition[0], colorsToAddOnPosition[1])
                        3-> positions[CornerPosition(
                            CubeCoordinates(height, depht, width),
                            colorsToAddOnPosition[0],
                            colorsToAddOnPosition[1],
                            colorsToAddOnPosition[2]
                        )] = Corner(colorsToAddOnPosition[0], colorsToAddOnPosition[1], colorsToAddOnPosition[2])
                    }

                }
            }
        }
        initAdjacency()
    }

    fun initAdjacency()
    {
        for(elem in positions.keys)
        {
            adjacencyList[elem] = positions.keys.filter { it.isAdjacent(elem) }
        }
    }

    fun clone() : Cube
    {
        var newCube = Cube(cubeSize)

        newCube.positions.clear()
        newCube.adjacencyList.clear()

        for((key, elem) in positions)
        {
            newCube.positions[key] = elem.clone()
        }

        return newCube
    }


    private fun colorToDisplayedString(color : Color) : String
    {
        when(color)
        {
            Color.BLUE -> with(TermColors()){ return((hsv(200,100,72) on hsv(200, 100, 72))("BBB"))}
            Color.GREEN -> with(TermColors()){ return((hsv(196,100,72) on hsv(196,100,72))("GGG"))}
            Color.RED -> with(TermColors()){ return((hsv(0,100,72) on hsv(0,100, 72))("RRR"))}
            Color.ORANGE -> with(TermColors()){ return((hsv(284,100,91) on hsv(284,100,92))("OOO"))}
            Color.WHITE -> with(TermColors()){ return((hsv(20,100,20) on hsv(20, 100, 20))("WWW"))}
            Color.YELLOW -> with(TermColors()){ return((hsv(32,100,100) on hsv(32,100,100))("YYY"))}
        }
    }


    fun display()
    {
        for(color in Color.values())
        {
            var side = positions.keys.filter {it.possessColor(color)}
                .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cubeSize).coordX }
                .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cubeSize).coordY }

            for(i in 0 until cubeSize*cubeSize)
            {
                if((i%cubeSize)==0)
                {
                    println()
                }
                //print(side[i].cubeCoordinates.getSideCoordinate(color, cubeSize).coordX.toString() + " " + side[i].cubeCoordinates.getSideCoordinate(color, cubeSize).coordY.toString())
                var piece = positions[side[i]]!!
                if(piece is Corner)
                {
                    if((side[i] as CornerPosition).colorOne==color) print(colorToDisplayedString(piece.colorOne))
                    if((side[i] as CornerPosition).colorTwo==color) print(colorToDisplayedString(piece.colorTwo))
                    if((side[i] as CornerPosition).colorThree==color) print(colorToDisplayedString(piece.colorThree))
                }
                if(piece is Edge)
                {
                    if((side[i] as EdgePosition).colorOne==color) print(colorToDisplayedString(piece.colorOne))
                    if((side[i] as EdgePosition).colorTwo==color) print(colorToDisplayedString(piece.colorTwo))
                }
                if(piece is Center)
                {
                    print(colorToDisplayedString(piece.colorOne))
                }
            }
            println()
        }
    }

    private fun displayEmptyLine()
    {
        for(i in 0..10)
        {
            print(" ")
        }
    }

}
