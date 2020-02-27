package app.service.cube

import app.model.cubeUtils.Color
import app.model.cubeUtils.Movement
import app.model.cube.Cube
import app.model.cube.piece.Corner
import app.model.cube.piece.Edge
import app.model.cube.piece.Piece
import app.model.cube.position.Position

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

        var clockwise = !movement.toString().endsWith("REVERSE")

        var side = cube.positions.keys.filter {it.possessColor(colorOfMovement)}
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube.cubeSize).coordX }
            .sortedBy { it.cubeCoordinates.getSideCoordinate(colorOfMovement, cube.cubeSize).coordY }

        var newSide = HashMap<Position, Piece>()

        while(side.size > 1)
        {
            for(j in 0 until Math.sqrt(side.size.toDouble()).toInt()-1)
            {

                var a = j
                var b = (j+1)*(Math.sqrt(side.size.toDouble()).toInt())-1
                var c = side.size-j-1
                var d = side.size-(j+1)*(Math.sqrt(side.size.toDouble()).toInt())

                if(clockwise)
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
            }
            side = side.filterNot { newSide.containsKey(it) }
        }

        for((position, piece) in newSide)
        {
            if(piece is Edge && (colorOfMovement== Color.GREEN || colorOfMovement== Color.BLUE))
            {
                var temp = piece.colorOne
                piece.colorOne = piece.colorTwo
                piece.colorTwo = temp
            }

            if(piece is Corner)
            {
                if(colorOfMovement == Color.YELLOW || colorOfMovement == Color.WHITE)
                {
                    var temp = piece.colorTwo
                    piece.colorTwo = piece.colorThree
                    piece.colorThree = temp
                }
                if(colorOfMovement == Color.GREEN || colorOfMovement == Color.BLUE)
                {
                    var temp = piece.colorOne
                    piece.colorOne = piece.colorThree
                    piece.colorThree = temp
                }
                if(colorOfMovement == Color.RED || colorOfMovement == Color.ORANGE)
                {
                    var temp = piece.colorOne
                    piece.colorOne = piece.colorTwo
                    piece.colorTwo = temp
                }
            }
        }

        cube.positions = cube.positions.filterNot { newSide.contains(it.key) } as HashMap<Position, Piece>
        cube.positions.putAll(newSide)
    }

}