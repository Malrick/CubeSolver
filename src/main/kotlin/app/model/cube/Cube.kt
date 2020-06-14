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
import app.model.movement.RelativePosition
import app.model.orientation.Orientation
import app.service.cube.CubeInformationService
import kotlin.properties.Delegates

class Cube {

    var cubeSize : Int by Delegates.notNull()
    var positions = HashMap<Position, Piece>()
    var adjacencyList = HashMap<Position, List<Position>>()
    var orientation : Orientation by Delegates.notNull()

    constructor(cubeSize : Int, orientation : Orientation)
    {
        this.cubeSize = cubeSize
        this.orientation = orientation

        for(height in 0 until cubeSize)
        {
            for(depht in 0 until cubeSize)
            {
                for(width in 0 until cubeSize)
                {
                    var colorsToAddOnPosition = ArrayList<Color>()

                    if(height == 0)          colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.BOTTOM]!!)
                    if(height == cubeSize-1) colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.TOP]!!)

                    if(depht == 0)           colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.FRONT]!!)
                    if(depht == cubeSize-1)  colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.BACK]!!)

                    if(width == 0)           colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.LEFT]!!)
                    if(width == cubeSize-1)  colorsToAddOnPosition.add(orientation.colorPositions[RelativePosition.RIGHT]!!)

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
        var clone = Cube(cubeSize, orientation)
        clone.positions.clear()

        for((key, elem) in positions)
        {
            clone.positions[key] = elem.clone()
        }

        clone.initAdjacency()
        return clone
    }

}
