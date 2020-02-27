package app.model.cubeOLD

import app.model.cubeUtils.Color
import app.model.cubeOLD.constants.EdgePosition

class Edge(private var identity: EdgePosition,
           var colorOne: Color,
           var colorTwo: Color
) {

    private var solved = false
    private var position = EdgePosition.A

    fun setPosition(edgePosition: EdgePosition)
    {
        this.position = edgePosition
    }

    fun setSolved(solved : Boolean)
    {
        this.solved = solved
    }

    fun getPosition() : EdgePosition
    {
        return position
    }

    fun isSolved() : Boolean
    {
        return solved
    }

    fun isBadlyOriented() : Boolean
    {
        return ((identity == position) && !solved)
    }

    fun getIdentity() : EdgePosition
    {
        return identity
    }
}