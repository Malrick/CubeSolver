package app.service

import app.model.Corner
import app.model.constants.Color
import org.koin.core.KoinComponent

class CornerService : KoinComponent {

    fun findCornerByName(corners : Array<Corner>, name : Char): Corner? {
        for(corner in corners)
        {
            if(corner.getName() == name)
            {
                return corner
            }
        }
        return null
    }

    fun findCornerByPosition(corners : Array<Corner>, position : Char) : Corner? {
        for(corner in corners)
        {
            if(corner.getPosition() == position)
            {
                return corner
            }
        }
        return null
    }

    fun cornerIncludesColor(corner : Corner, color : Color) : Boolean
    {
        return (corner.colorOne == color || corner.colorTwo == color || corner.colorThree == color)
    }

}