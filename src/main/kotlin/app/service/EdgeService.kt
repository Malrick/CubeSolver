package app.service

import app.model.Corner
import app.model.Side
import app.model.Edge
import app.model.constants.Color

class EdgeService {

    fun findEdgeByName(edges : Array<Edge>, name : Char): Edge? {
        for(edge in edges)
        {
            if(edge.getName() == name)
            {
                return edge
            }
        }
        return null
    }

    fun findEdgeByPosition(edges : Array<Edge>, position : Char) : Edge? {
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