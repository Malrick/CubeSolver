package app.service.cube

import app.model.Color
import app.model.cube.Cube
import app.model.cube.lookups.EdgePositionsIdentities
import app.model.cube.piece.Piece
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.cube.position.Position
import app.model.orientation.RelativePosition
import app.model.orientation.Orientation
import app.service.orientation.OrientationService
import org.koin.core.KoinComponent
import org.koin.core.inject
class CubeInformationService : KoinComponent{

    private val orientationService : OrientationService by inject()

    fun isSolved(cube : Cube) : Boolean
    {
        return cube.positions.all { it.key.matches(it.value) }
    }

    fun getNumberOfSolved(cube : Cube) : Int
    {
        return cube.positions.filter { it.key.matches(it.value) }.size
    }

    fun getSideByRelativePosition(cube : Cube, relativePosition : RelativePosition) : ArrayList<Color> {

        var toReturn = ArrayList<Color>()

        var sortedKeys = cube.positions.keys.filter { it.possessColor(cube.orientation.colorPositions[relativePosition]!!) }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(cube.orientation.colorPositions[relativePosition]!!, cube)!!.coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(cube.orientation.colorPositions[relativePosition]!!, cube)!!.coordY }

        for(elem in sortedKeys)
        {
            toReturn.add(cube.positions[elem]!!.getColorAtPosition(elem.positionOfColor(cube.orientation.colorPositions[relativePosition]!!))!!)
        }

        return toReturn
    }

    fun getSideByColor(cube : Cube, color : Color) : ArrayList<Color> {

        var toReturn = ArrayList<Color>()

        var sortedKeys = cube.positions.keys.filter { it.possessColor(color) }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube)!!.coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(color, cube)!!.coordY }

        for(elem in sortedKeys)
        {
            toReturn.add(cube.positions[elem]!!.getColorAtPosition(elem.positionOfColor(color))!!)
        }

        return toReturn
    }

    fun getNumberOfCornersSolvedBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(color) && it.key is CornerPosition && it.key.matches(it.value)}.size
    }

    fun getNumberOfEdgesSolvedBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(color) && it.key is EdgePosition && it.key.matches(it.value)}.size
    }

    fun getNumberOfEdgesOfAColorSolved(cube : Cube, colorOne : Color, colorTwo : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(colorOne) && it.key.possessColor(colorTwo) && it.key is EdgePosition && it.key.matches(it.value)}.size
    }

    fun getNumberOfPiecesOfAColorSolved(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key.possessColor(color) && it.key.matches(it.value) }.size
    }

    fun getNumberOfPiecesOfAColorBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.value.getColorAtPosition(it.key.positionOfColor(color)) == color }.size
    }

    fun getNumberOfCornersOfAColorBySide(cube : Cube, color : Color) : Int
    {
        return cube.positions.filter { it.key is CornerPosition && it.value.getColorAtPosition(it.key.positionOfColor(color)) == color }.size
    }

    fun isCornerOfAColorSolved(cube : Cube, colorOne : Color, colorTwo : Color, colorThree : Color) : Boolean
    {
        var position = cube.positions.filter { it.key.possessColor(colorOne) && it.key.possessColor(colorTwo) && it.key.possessColor(colorThree)}.keys.first()
        return position.matches(cube.positions[position]!!)
        //return cube.positions.any { it.key.matches(it.value) && it.key.possessColor(colorOne) && it.key.possessColor(colorTwo) && it.key.possessColor(colorThree)}
    }

    fun allCornersAreSolved(cube : Cube) : Boolean
    {
        return cube.positions.filter { it.key is CornerPosition }.all { it.key.matches(it.value) }
    }

    fun getPositionsOfEdge(cube : Cube, colorOne : Color, colorTwo : Color) : Position
    {
        return cube.positions.filter { it.key is EdgePosition && it.value.possessColor(colorOne) && it.value.possessColor(colorTwo) }.keys.first()
    }

    fun allEdgesToTheirSlices(cube : Cube) : Boolean
    {
        return allMEdgesInMSlice(cube) && allEEdgesInESlice(cube) && allSEdgesInSSlice(cube)
    }

    fun allMEdgesInMSlice(cube : Cube) : Boolean
    {
        var TF = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.TopFront]!!]!!
        var TB = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.TopBack]!!]!!
        var BF = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomFront]!!]!!
        var BB = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomBack]!!]!!
        if(listOf(TF, TB, BF, BB).any { it.getColors().contains(cube.orientation.colorPositions[RelativePosition.LEFT]!!) ||  it.getColors().contains(cube.orientation.colorPositions[RelativePosition.RIGHT]!!)}) return false
        return true
    }

    fun allEEdgesInESlice(cube : Cube) : Boolean
    {
        var FL = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.FrontLeft]!!]!!
        var FR = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.FrontRight]!!]!!
        var BL = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BackLeft]!!]!!
        var BR = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BackRight]!!]!!
        if(listOf(FL, FR, BL, BR).any { it.getColors().contains(cube.orientation.colorPositions[RelativePosition.TOP]!!) ||  it.getColors().contains(cube.orientation.colorPositions[RelativePosition.BOTTOM]!!)}) return false
        return true
    }

    fun allSEdgesInSSlice(cube : Cube) : Boolean
    {
        var TL = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.TopLeft]!!]!!
        var TR = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.TopRight]!!]!!
        var BL = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomLeft]!!]!!
        var BR = cube.positions[cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomRight]!!]!!
        if(listOf(TL, TR, BL, BR).any { it.getColors().contains(cube.orientation.colorPositions[RelativePosition.FRONT]!!) ||  it.getColors().contains(cube.orientation.colorPositions[RelativePosition.BACK]!!)}) return false
        return true
    }

    fun numberOfEEdgesSolved(cube : Cube) : Int
    {
        var FL = cube.lookUps.edgeLookup[EdgePositionsIdentities.FrontLeft]!!
        var FR = cube.lookUps.edgeLookup[EdgePositionsIdentities.FrontRight]!!
        var BL = cube.lookUps.edgeLookup[EdgePositionsIdentities.BackLeft]!!
        var BR = cube.lookUps.edgeLookup[EdgePositionsIdentities.BackRight]!!
        return listOf(FL, FR, BL, BR).count{it.matches(cube.positions[it]!!)}
    }

    fun numberOfEdgesInBottomSideWithoutColor(cube : Cube, color:Color) : Int
    {
        var BL = cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomLeft]!!
        var BR = cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomRight]!!
        var BB = cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomBack]!!
        var BF = cube.lookUps.edgeLookup[EdgePositionsIdentities.BottomFront]!!
        return listOf(BL, BR, BB, BF).count{!it.possessColor(color)}
    }

    // A cube is said to possess integrity if it corresponds to a valid scramble.
    // Many different tests will be made :
    // - Each color is appearing the same number of times on the cube
    // - Each piece exists, and exists only once
    // TODO The parity of the number of edge swaps required to solve the cube's edges is equal to
    //   the parity of the number of corners swaps required to solve the cube's corners
    // - The orientations of the different pieces are coherent with each other
    // If our cube pass all these tests, we can guarantee that our cube is valid, and can be solved.
    fun integrityCheck(cube : Cube) : Boolean
    {
        // Initializing
        var colorCounters = HashMap<Color, Int>()
        for(color in Color.values())
        {
            colorCounters[color] = 0
        }

        // Counting
        for((position, piece) in cube.positions)
        {
            for(color in piece.getColors())
            {
                colorCounters[color] = colorCounters[color]?.plus(1)!!
            }
        }

        // Checking
        for(color in Color.values())
        {
            if(colorCounters[color]!=9)
            {
                println("Wrong number of colors")
                return false
            }
        }

        var positionsColorSets = mutableSetOf(setOf<Color>())
        var piecesColorSets = mutableSetOf(setOf<Color>())

        for((position, piece) in cube.positions)
        {
            positionsColorSets.add(position.getColors())
            piecesColorSets.add(piece.getColors())
        }

        if(positionsColorSets != piecesColorSets)
        {
            println("Not every piece exist")
            return false
        }

        if(getCornerOrientations(cube).sum()%3 != 0)
        {
            println("Corner orientation is wrong")
            return false
        }

        if(getEdgeOrientations(cube).sum()%2!=0)
        {
            println("Edge orientation is wrong")
            return false
        }

        return true
    }

    /*
        It could be nice to output orientations as an hashmap (a piece is linked to its orientation)
        But the way it is used for now on the project, it would be too time consuming.
        As as result, only an array of Ints is given.
     */
    fun getCornerOrientations(cube : Cube) : Array<Int>
    {
        var sortedCornersPositions = cube.lookUps.sortedCornersPositionLookup
        var cornerOrientations = Array(8){0}
        var i = 0
        var leftColor = cube.orientation.colorPositions[RelativePosition.LEFT]!!
        var rightColor = cube.orientation.colorPositions[RelativePosition.RIGHT]!!

        var isTopCorner : Boolean
        var isLeftCorner : Boolean
        var isFrontCorner: Boolean
        var colorToSearch : Color

        for(cornerPosition in sortedCornersPositions)
        {
            isTopCorner = cornerPosition.cubeCoordinates.height == cube.cubeSize-1
            isLeftCorner = cornerPosition.cubeCoordinates.width == 0
            isFrontCorner = cornerPosition.cubeCoordinates.depht == 0

            var piece = cube.positions[cornerPosition]!!

            if(piece.possessColor(leftColor))
            {
                colorToSearch = leftColor
            }
            else
            {
                colorToSearch = rightColor
            }

            when(piece.getPositionOfColor(colorToSearch))
            {
                1 -> {
                    if (i == 0 || i == 3 || i == 5 || i == 6)
                    {
                        cornerOrientations[i]= 1
                    } else {
                        cornerOrientations[i]= 2
                    }
                }
                2 -> {
                    if (i == 0 || i == 3 || i == 5 || i == 6)
                    {
                        cornerOrientations[i]= 2
                    }
                    else
                    {
                        cornerOrientations[i]= 1
                    }
                }
            }
            i++
        }
        return cornerOrientations
    }

    /*
        Same remark as on corner orientation, only an array is given for now.
     */
    fun getEdgeOrientations(cube : Cube) : Array<Int>
    {
        var edgeOrientations = Array(12){0}

        var i = 0
        for((position, piece) in cube.positions.filter { it.key is EdgePosition })
        {
            var edgePossessTop = piece.possessColor(cube.orientation.colorPositions[RelativePosition.FRONT]!!)
            var edgePossessBot = piece.possessColor(cube.orientation.colorPositions[RelativePosition.BACK]!!)
            var edgePossessLeft = piece.possessColor(cube.orientation.colorPositions[RelativePosition.LEFT]!!)
            var edgePossessRight = piece.possessColor(cube.orientation.colorPositions[RelativePosition.RIGHT]!!)

            var positionPossessTop = position.possessColor(cube.orientation.colorPositions[RelativePosition.FRONT]!!)
            var positionPossessBot = position.possessColor(cube.orientation.colorPositions[RelativePosition.BACK]!!)
            var positionPossessLeft = position.possessColor(cube.orientation.colorPositions[RelativePosition.LEFT]!!)
            var positionPossessRight = position.possessColor(cube.orientation.colorPositions[RelativePosition.RIGHT]!!)

            if(edgePossessTop || edgePossessBot)
            {
                var edgePossessedColor : Color
                if(edgePossessTop) edgePossessedColor = cube.orientation.colorPositions[RelativePosition.FRONT]!!
                else edgePossessedColor = cube.orientation.colorPositions[RelativePosition.BACK]!!


                var positionPossessedColor : Color
                if(positionPossessTop) positionPossessedColor = cube.orientation.colorPositions[RelativePosition.FRONT]!!
                else positionPossessedColor = cube.orientation.colorPositions[RelativePosition.BACK]!!

                if(position.positionOfColor(positionPossessedColor) == piece.getPositionOfColor(edgePossessedColor))
                {
                    edgeOrientations[i] = 1
                }
            }
            if(edgePossessLeft || edgePossessRight)
            {
                var edgePossessedColor : Color
                if(edgePossessLeft) edgePossessedColor = cube.orientation.colorPositions[RelativePosition.LEFT]!!
                else edgePossessedColor = cube.orientation.colorPositions[RelativePosition.RIGHT]!!


                var positionPossessedColor : Color
                if(positionPossessLeft) positionPossessedColor = cube.orientation.colorPositions[RelativePosition.LEFT]!!
                else positionPossessedColor = cube.orientation.colorPositions[RelativePosition.RIGHT]!!

                if(position.positionOfColor(positionPossessedColor) == piece.getPositionOfColor(edgePossessedColor))
                {
                    edgeOrientations[i] = 1
                }
            }
            if((positionPossessTop || positionPossessBot) && (edgePossessLeft || edgePossessRight))
            {
                var edgePossessedColor : Color
                if(edgePossessLeft) edgePossessedColor = cube.orientation.colorPositions[RelativePosition.LEFT]!!
                else edgePossessedColor = cube.orientation.colorPositions[RelativePosition.RIGHT]!!


                var positionPossessedColor : Color
                if(positionPossessTop) positionPossessedColor = cube.orientation.colorPositions[RelativePosition.FRONT]!!
                else positionPossessedColor = cube.orientation.colorPositions[RelativePosition.BACK]!!

                if(position.positionOfColor(positionPossessedColor) != piece.getPositionOfColor(edgePossessedColor))
                {
                    edgeOrientations[i] = 1
                }
            }
            if((positionPossessLeft || positionPossessRight) && (edgePossessTop || edgePossessBot))
            {
                var edgePossessedColor : Color
                if(edgePossessTop) edgePossessedColor = cube.orientation.colorPositions[RelativePosition.FRONT]!!
                else edgePossessedColor = cube.orientation.colorPositions[RelativePosition.BACK]!!


                var positionPossessedColor : Color
                if(positionPossessLeft) positionPossessedColor = cube.orientation.colorPositions[RelativePosition.LEFT]!!
                else positionPossessedColor = cube.orientation.colorPositions[RelativePosition.RIGHT]!!

                if(position.positionOfColor(positionPossessedColor) != piece.getPositionOfColor(edgePossessedColor))
                {
                    edgeOrientations[i] = 1
                }
            }
            i++
        }

        return edgeOrientations
    }

    /*
        Not used for now on the project, but this functionality seems interesting to me.
        It gets the different coherent/adjacents groups of pieces on the cube.
     */
    fun getGroups(cube : Cube) : Array<Array<Piece>>
    {
        var positionAssignedToAGroup = HashMap<Position, Boolean>()
        var phantomToPosition = HashMap<Orientation, Set<Position>>()

        for((position, piece) in cube.positions)
        {
            positionAssignedToAGroup[position] = false
            var topBot = when(position.cubeCoordinates.height)
            {
                0 -> RelativePosition.BOTTOM
                cube.cubeSize-1 -> RelativePosition.TOP
                else -> null
            }
            var frontBack = when(position.cubeCoordinates.depht)
            {
                0 -> RelativePosition.FRONT
                cube.cubeSize-1 -> RelativePosition.BACK
                else -> null
            }
            var leftRight = when(position.cubeCoordinates.width)
            {
                0-> RelativePosition.LEFT
                cube.cubeSize-1 -> RelativePosition.RIGHT
                else -> null
            }

            var phantomOrientations = setOf<Orientation>()

            if(topBot != null && frontBack != null)
            {
                phantomOrientations += orientationService.getOrientation(Pair(topBot, piece.getColorAtPosition(1)!!), Pair(frontBack, piece.getColorAtPosition(2)!!))
            }
            else if(topBot != null && leftRight != null)
            {
                phantomOrientations += orientationService.getOrientation(Pair(topBot, piece.getColorAtPosition(1)!!), Pair(leftRight, piece.getColorAtPosition(2)!!))
            }
            else if(frontBack != null && leftRight != null)
            {
                phantomOrientations += orientationService.getOrientation(Pair(frontBack, piece.getColorAtPosition(1)!!), Pair(leftRight, piece.getColorAtPosition(2)!!))
            }
            else if(topBot != null)
            {
                phantomOrientations += orientationService.getOrientations(Pair(topBot, piece.getColorAtPosition(1)!!))
            }
            else if(leftRight != null)
            {
                phantomOrientations += orientationService.getOrientations(Pair(leftRight, piece.getColorAtPosition(1)!!))
            }
            else if(frontBack != null)
            {
                phantomOrientations += orientationService.getOrientations(Pair(frontBack, piece.getColorAtPosition(1)!!))
            }

            for(phantomOrientation in phantomOrientations)
            {
                if(phantomToPosition.any { it.key.equals(phantomOrientation)})
                {
                    phantomToPosition[phantomToPosition.filter{ it.key.equals(phantomOrientation)}.keys.first()] = phantomToPosition[phantomToPosition.filter{ it.key.equals(phantomOrientation)}.keys.first()]?.union(setOf(position))!!
                }
                else if(phantomToPosition[phantomOrientation] == null)
                {
                    phantomToPosition[phantomOrientation] = setOf(position)
                }
            }

        }

        var groups = arrayOf<Array<Position>>()

        for((position, isAssigned) in positionAssignedToAGroup)
        {
            if(!isAssigned)
            {
                var sameOrientation = phantomToPosition.filter { it.value.contains(position)}.maxBy { it.value.filter { positionAssignedToAGroup[it] == false }.size }!!.value.filter { positionAssignedToAGroup[it] == false }

                var group = arrayOf(position)

                var groupmates = cube.adjacencyList[position]!!.filter{sameOrientation.contains(it)}.toList()

                var doneGroupmates = setOf<Position>()

                while(!groupmates.isEmpty())
                {
                    groupmates += cube.adjacencyList[groupmates[0]]!!.filter{sameOrientation.contains(it) && !group.contains(it) && !groupmates.contains(it) && !doneGroupmates.contains(it)}.toTypedArray()
                    group += groupmates[0]
                    doneGroupmates = doneGroupmates.union(setOf(groupmates[0]))
                    groupmates = groupmates.drop(1)
                }
                for(elem in group)
                {
                    positionAssignedToAGroup[elem] = true
                }

                groups += group
            }
        }

        var myPieces = arrayOf<Piece>()
        var toReturn = arrayOf<Array<Piece>>()

        for(group in groups)
        {
            for(position in group)
            {
                myPieces += cube.positions[position]!!
            }
            toReturn+= myPieces
            myPieces = arrayOf()
        }

        return toReturn
    }



}