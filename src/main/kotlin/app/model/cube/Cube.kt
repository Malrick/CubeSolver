package app.model.cube

import app.model.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Center
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import app.model.cube.position.CenterPosition
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.cube.position.Position
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

                    if(height == 0)          colorsToAddOnPosition.add(Color.YELLOW)
                    if(height == cubeSize-1) colorsToAddOnPosition.add(Color.WHITE)

                    if(depht == 0)           colorsToAddOnPosition.add(Color.GREEN)
                    if(depht == cubeSize-1)  colorsToAddOnPosition.add(Color.BLUE)

                    if(width == 0)           colorsToAddOnPosition.add(Color.ORANGE)
                    if(width == cubeSize-1)  colorsToAddOnPosition.add(Color.RED)

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
        for(elem in positions.keys)
        {
            adjacencyList[elem] = positions.keys.filter { it.isAdjacent(elem) }
        }
    }

    fun clone() : Cube
    {
        var clone = Cube(cubeSize)

        clone.positions.clear()

        for((key, elem) in positions)
        {
            clone.positions[key] = elem.clone()
        }

        return clone
    }

    // A cube is said to be "integre" if it is valid, and solvable.
    // Many different tests will be made :
    // - Each color is appearing the same number of times on the cube
    // - Each piece exists, and exists only once
    // - The parity of the number of edge swaps required to solve the cube's edges is equal to
    //   the parity of the number of corners swaps required to solve the cube's corners
    // - The orientations of the different pieces are coherent with each other
    // Thanks to all these tests, we can guarantee that our cube is valid, and can be solved.
    fun integrityCheck() : Boolean
    {
        // Initializing
        var colorCounters = HashMap<Color, Int>()
        for(color in Color.values())
        {
            colorCounters[color] = 0
        }

        // Counting
        for((position, piece) in positions)
        {
            for(color in piece.getColors())
            {
                colorCounters[color]?.plus(1)
            }
        }

        // Checking
        for(color in Color.values())
        {
            if(colorCounters[color]!=9) return false
        }

        var positionsColorSets = mutableSetOf(setOf<Color>())
        var piecesColorSets = mutableSetOf(setOf<Color>())

        for((position, piece) in positions)
        {
           positionsColorSets.add(position.getColors())
           piecesColorSets.add(piece.getColors())
        }

        if(positionsColorSets != piecesColorSets) return false



        return true
    }
}
