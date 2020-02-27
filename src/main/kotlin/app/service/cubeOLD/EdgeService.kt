package app.service.cubeOLD

import app.model.cubeOLD.Edge
import app.model.cubeUtils.Color
import app.model.cubeOLD.constants.EdgePosition

class EdgeService {

    fun findEdgeByName(edges : Array<Edge>, name : EdgePosition): Edge? {
        for(edge in edges)
        {
            if(edge.getIdentity() == name)
            {
                return edge
            }
        }
        return null
    }

    fun findEdgeByPosition(edges : Array<Edge>, position : EdgePosition) : Edge? {
        for(edge in edges)
        {
            if(edge.getPosition() == position)
            {
                return edge
            }
        }
        return null
    }

    fun edgeIncludesColor(edge : Edge, color : Color) : Boolean
    {
        return (edge.colorOne == color || edge.colorTwo == color)
    }

}