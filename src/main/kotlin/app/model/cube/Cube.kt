package app.model.cube

import app.model.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.lookups.CornerPositionsIdentities
import app.model.cube.lookups.CubeLookups
import app.model.cube.lookups.EdgePositionsIdentities
import app.model.cube.lookups.RotationLookup
import app.model.cube.piece.Center
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import app.model.cube.position.CenterPosition
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.cube.position.Position
import app.model.orientation.RelativePosition
import app.model.orientation.Orientation
import kotlin.properties.Delegates

class Cube {

    var cubeSize : Int by Delegates.notNull()
    var positions = HashMap<Position, Piece>()
    var adjacencyList = HashMap<Position, List<Position>>()
    var orientation : Orientation by Delegates.notNull()
    lateinit var lookUps : CubeLookups

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
        initLookups()
    }

    fun initAdjacency()
    {
        for(elem in positions.keys)
        {
            adjacencyList[elem] = positions.keys.filter { it.isAdjacent(elem) }
        }
    }


    fun initLookups()
    {
        var cornerA : Position
        var cornerB : Position
        var cornerC : Position
        var cornerD : Position
        var edgeA : Position
        var edgeB : Position
        var edgeC : Position
        var edgeD : Position

        var rotationLookups = HashMap<Color, RotationLookup>()
        var sortedCornersPositionLookup : List<CornerPosition>
        var sortedEdgesPositionLookup : List<EdgePosition>
        var edgeLookup = HashMap<EdgePositionsIdentities, EdgePosition>()
        var cornerLookup = HashMap<CornerPositionsIdentities, CornerPosition>()

        rotationLookups = HashMap()

        for(color in Color.values())
        {
            when(color)
            {
                Color.WHITE ->
                {
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.BLUE) && it.possessColor(Color.ORANGE)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.BLUE) && it.possessColor(Color.RED)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.RED) && it.possessColor(Color.GREEN)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.GREEN) && it.possessColor(Color.ORANGE)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.BLUE)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.RED)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.GREEN)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.ORANGE)}
                }
                Color.ORANGE ->{
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.BLUE) && it.possessColor(Color.ORANGE)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.WHITE) &&  it.possessColor(Color.ORANGE) && it.possessColor(Color.GREEN)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.ORANGE) && it.possessColor(Color.GREEN)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.ORANGE) && it.possessColor(Color.BLUE)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.ORANGE) &&  it.possessColor(Color.WHITE)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.ORANGE) &&  it.possessColor(Color.GREEN)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.ORANGE) &&  it.possessColor(Color.YELLOW)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.ORANGE) &&  it.possessColor(Color.BLUE)}
                }
                Color.GREEN -> {
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.ORANGE)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.RED)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.RED)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.ORANGE)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.WHITE)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.RED)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.YELLOW)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.GREEN) &&  it.possessColor(Color.ORANGE)}
                }
                Color.RED -> {
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.GREEN)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.BLUE)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.BLUE)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.GREEN)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.WHITE)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.BLUE)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.YELLOW)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.RED) &&  it.possessColor(Color.GREEN)}
                }
                Color.YELLOW ->{
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.GREEN) && it.possessColor(Color.ORANGE)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.GREEN) && it.possessColor(Color.RED)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.RED) && it.possessColor(Color.BLUE)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.BLUE) && it.possessColor(Color.ORANGE)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.GREEN)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.RED)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.BLUE)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.YELLOW) &&  it.possessColor(Color.ORANGE)}
                }
                Color.BLUE -> {
                    cornerA = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.ORANGE)}
                    cornerB = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.YELLOW) && it.possessColor(Color.RED)}
                    cornerC = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.RED)}
                    cornerD = positions.keys.filter { it is CornerPosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.WHITE) && it.possessColor(Color.ORANGE)}
                    edgeA = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.YELLOW)}
                    edgeB = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.RED)}
                    edgeC = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.WHITE)}
                    edgeD = positions.keys.filter { it is EdgePosition }.first { it.possessColor(Color.BLUE) &&  it.possessColor(Color.ORANGE)}
                }

            }
            rotationLookups[color] = RotationLookup(
                cornerA,
                cornerB,
                cornerC,
                cornerD,
                edgeA,
                edgeB,
                edgeC,
                edgeD
            )
        }

        sortedCornersPositionLookup = positions.keys.filter { it is CornerPosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending { it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height } as List<CornerPosition>
        sortedEdgesPositionLookup = positions.keys.filter { it is EdgePosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending { it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height } as List<EdgePosition>

        edgeLookup = HashMap()

        for(i in sortedEdgesPositionLookup.indices)
        {
            when(i)
            {
                0 -> edgeLookup[EdgePositionsIdentities.TopBack] = sortedEdgesPositionLookup[i]
                1 -> edgeLookup[EdgePositionsIdentities.TopLeft] = sortedEdgesPositionLookup[i]
                2 -> edgeLookup[EdgePositionsIdentities.TopRight] = sortedEdgesPositionLookup[i]
                3 -> edgeLookup[EdgePositionsIdentities.TopFront] = sortedEdgesPositionLookup[i]
                4 -> edgeLookup[EdgePositionsIdentities.BackLeft] = sortedEdgesPositionLookup[i]
                5 -> edgeLookup[EdgePositionsIdentities.BackRight] = sortedEdgesPositionLookup[i]
                6 -> edgeLookup[EdgePositionsIdentities.FrontLeft] = sortedEdgesPositionLookup[i]
                7 -> edgeLookup[EdgePositionsIdentities.FrontRight] = sortedEdgesPositionLookup[i]
                8 -> edgeLookup[EdgePositionsIdentities.BottomBack] = sortedEdgesPositionLookup[i]
                9 -> edgeLookup[EdgePositionsIdentities.BottomLeft] = sortedEdgesPositionLookup[i]
                10 -> edgeLookup[EdgePositionsIdentities.BottomRight] = sortedEdgesPositionLookup[i]
                11 -> edgeLookup[EdgePositionsIdentities.BottomFront] = sortedEdgesPositionLookup[i]
            }
        }

        cornerLookup = HashMap()

        for(i in sortedCornersPositionLookup.indices)
        {
            when(i)
            {
                0 -> cornerLookup[CornerPositionsIdentities.TopBackLeft] = sortedCornersPositionLookup[i]
                1 -> cornerLookup[CornerPositionsIdentities.TopBackRight] = sortedCornersPositionLookup[i]
                2 -> cornerLookup[CornerPositionsIdentities.TopFrontLeft] = sortedCornersPositionLookup[i]
                3 -> cornerLookup[CornerPositionsIdentities.TopFrontRight] = sortedCornersPositionLookup[i]
                4 -> cornerLookup[CornerPositionsIdentities.BottomBackLeft] = sortedCornersPositionLookup[i]
                5 -> cornerLookup[CornerPositionsIdentities.BottomBackRight] = sortedCornersPositionLookup[i]
                6 -> cornerLookup[CornerPositionsIdentities.BottomFrontLeft] = sortedCornersPositionLookup[i]
                7 -> cornerLookup[CornerPositionsIdentities.BottomFrontRight] = sortedCornersPositionLookup[i]
            }
        }

        this.lookUps = CubeLookups(rotationLookups, edgeLookup, cornerLookup, sortedCornersPositionLookup, sortedEdgesPositionLookup)
    }

    fun clone() : Cube
    {
        var clone = Cube(cubeSize, orientation)
        clone.positions.clear()

        for((key, elem) in positions)
        {
            clone.positions[key] = elem.clone()
        }
        clone.initLookups()
        clone.initAdjacency()
        return clone
    }

}
