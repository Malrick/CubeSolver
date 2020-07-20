package app.service.cube

import app.model.Color
import app.model.movement.Movement
import app.model.cube.Cube
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.orientation.RelativePosition

/*
    Manage movements applied to the cube
 */
class CubeMotionService {

    fun applySequence(cube : Cube, movements : Array<Movement>)
    {
        for(movement in movements)
        {
            applyMovement(cube, movement)
        }
    }

    fun applyMovement(cube : Cube, movement : Movement)
    {
        val colorOfMovement = Color.values().first { movement.toString().startsWith(it.toString())}

        var clockwise = !movement.name.endsWith("REVERSE")
        var double = movement.name.endsWith("DOUBLE")

        var rotationLookUp = cube.lookUps.rotationLookups[colorOfMovement]!!

        var cornerPositionA = rotationLookUp!!.cornerA
        var cornerPositionB = rotationLookUp!!.cornerB
        var cornerPositionC = rotationLookUp!!.cornerC
        var cornerPositionD = rotationLookUp!!.cornerD
        var edgePositionA = rotationLookUp!!.edgeA
        var edgePositionB = rotationLookUp!!.edgeB
        var edgePositionC = rotationLookUp!!.edgeC
        var edgePositionD = rotationLookUp!!.edgeD

        var cornerA = cube.positions[cornerPositionA]!!
        var cornerB = cube.positions[cornerPositionB]!!
        var cornerC = cube.positions[cornerPositionC]!!
        var cornerD = cube.positions[cornerPositionD]!!
        var edgeA = cube.positions[edgePositionA]!!
        var edgeB = cube.positions[edgePositionB]!!
        var edgeC = cube.positions[edgePositionC]!!
        var edgeD = cube.positions[edgePositionD]!!


        if(double)
        {
            var temp = cornerA
            cornerA = cornerC
            cornerC = temp

            temp = cornerB
            cornerB = cornerD
            cornerD = temp

            temp = edgeA
            edgeA = edgeC
            edgeC = temp

            temp = edgeB
            edgeB = edgeD
            edgeD = temp

        }
        else if(clockwise)
        {
            var temp = cornerD
            cornerD = cornerC
            cornerC = cornerB
            cornerB = cornerA
            cornerA = temp
            temp = edgeD
            edgeD = edgeC
            edgeC = edgeB
            edgeB = edgeA
            edgeA = temp
        }
        else
        {
            var temp = cornerA
            cornerA = cornerB
            cornerB = cornerC
            cornerC = cornerD
            cornerD = temp
            temp = edgeA
            edgeA = edgeB
            edgeB = edgeC
            edgeC = edgeD
            edgeD = temp
        }

        // The code beneath allows one to turn a side, even if the cube is not 3x3x3
        // It's not used here because it's time consuming

        /*var side = cube.positions.keys.filter {it.possessColor(colorOfMovement)}
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube)!!.coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube)!!.coordY }



        while(side.size > 1)
        {
            val lengthOfTreatedSquare = Math.sqrt(side.size.toDouble()).toInt()
            for(j in 0 until lengthOfTreatedSquare-1)
            {

                var a = side.size-(j+1)*(lengthOfTreatedSquare)
                var b = side.size-j-1
                var c = (j+1)*(lengthOfTreatedSquare)-1
                var d = j

                if(antiClockwise)
                {
                    var temp = a
                    a = d
                    d = temp
                    temp = b
                    b = c
                    c = temp
                }

                newSide[side.elementAt(a)] = cube.positions[side.elementAt(b)]!!
                newSide[side.elementAt(b)] = cube.positions[side.elementAt(c)]!!
                newSide[side.elementAt(c)] = cube.positions[side.elementAt(d)]!!
                newSide[side.elementAt(d)] = cube.positions[side.elementAt(a)]!!

                if(double)
                {
                    newSide[side.elementAt(a)] = cube.positions[side.elementAt(c)]!!
                    newSide[side.elementAt(b)] = cube.positions[side.elementAt(d)]!!
                    newSide[side.elementAt(c)] = cube.positions[side.elementAt(a)]!!
                    newSide[side.elementAt(d)] = cube.positions[side.elementAt(b)]!!
                }

            }
            side = side.filterNot { newSide.containsKey(it) }
        }*/

        if(!double) normalizeColors(cube, colorOfMovement, listOf(cornerA, cornerB, cornerC, cornerD) as List<Corner>, listOf(edgeA, edgeB, edgeC, edgeD) as List<Edge>)

        cube.positions[cornerPositionA] = cornerA
        cube.positions[cornerPositionB] = cornerB
        cube.positions[cornerPositionC] = cornerC
        cube.positions[cornerPositionD] = cornerD
        cube.positions[edgePositionA] = edgeA
        cube.positions[edgePositionB] = edgeB
        cube.positions[edgePositionC] = edgeC
        cube.positions[edgePositionD] = edgeD

        //cube.positions = cube.positions.filterNot { newSide.contains(it.key) } as HashMap<Position, Piece>
        //cube.positions.putAll(newSide)
    }

    // Some colors need to be switched to keep the cube coherent
    private fun normalizeColors(
        cube : Cube,
        colorOfMovement: Color,
        cornerList : List<Corner>,
        edgeList : List<Edge>
    ) {

        var frontColor = cube.orientation.getColorAtPosition(RelativePosition.FRONT)
        var backColor = cube.orientation.getColorAtPosition(RelativePosition.BACK)
        var leftColor = cube.orientation.getColorAtPosition(RelativePosition.LEFT)
        var rightColor = cube.orientation.getColorAtPosition(RelativePosition.RIGHT)
        var topColor = cube.orientation.getColorAtPosition(RelativePosition.TOP)
        var bottomColor = cube.orientation.getColorAtPosition(RelativePosition.BOTTOM)

        var temp: Color
        if (colorOfMovement == frontColor|| colorOfMovement == backColor) {
            for (edge in edgeList) {
                temp = edge.colorOne
                edge.colorOne = edge.colorTwo
                edge.colorTwo = temp
            }
        }
        for(corner in cornerList)
        {
            when(colorOfMovement)
            {
                topColor->{
                    temp = corner.colorTwo
                    corner.colorTwo = corner.colorThree
                    corner.colorThree = temp
                }
                bottomColor->{
                    temp = corner.colorTwo
                    corner.colorTwo = corner.colorThree
                    corner.colorThree = temp
                }
                frontColor->{
                    temp = corner.colorOne
                    corner.colorOne = corner.colorThree
                    corner.colorThree = temp
                }
                backColor->{
                    temp = corner.colorOne
                    corner.colorOne = corner.colorThree
                    corner.colorThree = temp
                }
                leftColor->{
                    temp = corner.colorOne
                    corner.colorOne = corner.colorTwo
                    corner.colorTwo = temp
                }
                rightColor->{
                    temp = corner.colorOne
                    corner.colorOne = corner.colorTwo
                    corner.colorTwo = temp
                }
            }
        }
    }
}
