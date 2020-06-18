package app.service.cube

import app.model.Color
import app.model.movement.Movement
import app.model.cube.Cube
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import app.model.cube.position.Position
import app.model.movement.RelativePosition

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

        var antiClockwise = movement.name.endsWith("REVERSE")
        var double = movement.name.endsWith("DOUBLE")

        var side = cube.positions.keys.filter {it.possessColor(colorOfMovement)}
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube)!!.coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube)!!.coordY }

        var newSide = HashMap<Position, Piece>()

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
        }

        normalizeColors(cube, newSide, colorOfMovement)

        if(double) normalizeColors(cube, newSide, colorOfMovement)

        cube.positions = cube.positions.filterNot { newSide.contains(it.key) } as HashMap<Position, Piece>
        cube.positions.putAll(newSide)
    }

    private fun normalizeColors(
        cube : Cube,
        newSide: HashMap<Position, Piece>,
        colorOfMovement: Color
    ) {
        for (piece in newSide.values) {
            if (piece is Edge && (colorOfMovement == cube.orientation.colorPositions[RelativePosition.FRONT] || colorOfMovement == cube.orientation.colorPositions[RelativePosition.BACK])) {
                var temp = piece.colorOne
                piece.colorOne = piece.colorTwo
                piece.colorTwo = temp
            }

            if (piece is Corner) {
                if (colorOfMovement == cube.orientation.colorPositions[RelativePosition.TOP] || colorOfMovement == cube.orientation.colorPositions[RelativePosition.BOTTOM]) {
                    var temp = piece.colorTwo
                    piece.colorTwo = piece.colorThree
                    piece.colorThree = temp
                }
                if (colorOfMovement == cube.orientation.colorPositions[RelativePosition.FRONT] || colorOfMovement == cube.orientation.colorPositions[RelativePosition.BACK]) {
                    var temp = piece.colorOne
                    piece.colorOne = piece.colorThree
                    piece.colorThree = temp
                }
                if (colorOfMovement == cube.orientation.colorPositions[RelativePosition.LEFT] || colorOfMovement == cube.orientation.colorPositions[RelativePosition.RIGHT]) {
                    var temp = piece.colorOne
                    piece.colorOne = piece.colorTwo
                    piece.colorTwo = temp
                }
            }
        }
    }

}