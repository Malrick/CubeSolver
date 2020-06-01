package app.model.cube.position

import app.model.cubeUtils.Color
import app.model.cube.coordinates.CubeCoordinates
import app.model.cube.piece.Piece
import kotlin.properties.Delegates

abstract class Position {

    abstract var cubeCoordinates : CubeCoordinates

    abstract fun possessColor(color : Color) : Boolean

    abstract fun positionOfColor(color : Color) : Int

    abstract fun matches(piece : Piece) : Boolean

    abstract fun getColors() : Set<Color>

    fun isAdjacent(position : Position) : Boolean
    {
        var numberOfEqualCoordinates =0

        if(cubeCoordinates.height == position.cubeCoordinates.height) numberOfEqualCoordinates++
        if(cubeCoordinates.width  == position.cubeCoordinates.width)  numberOfEqualCoordinates++
        if(cubeCoordinates.depht  == position.cubeCoordinates.depht)  numberOfEqualCoordinates++

        var sumOfMyCoordinates  =          cubeCoordinates.height +          cubeCoordinates.width +          cubeCoordinates.depht
        var sumOfHisCoordinates = position.cubeCoordinates.height + position.cubeCoordinates.width + position.cubeCoordinates.depht

        if(numberOfEqualCoordinates == 2 && sumOfHisCoordinates >= sumOfMyCoordinates-1 && sumOfHisCoordinates <= sumOfMyCoordinates+1) return true
        return false
    }

}