package app.service.cubeOLD

import app.model.cubeOLD.Corner
import app.model.cubeUtils.Color
import app.model.cubeOLD.constants.CornerPosition
import org.koin.core.KoinComponent

class CornerService : KoinComponent {

    fun findCornerByName(corners : Array<Corner>, name : CornerPosition): Corner? {
        for(corner in corners)
        {
            if(corner.getName() == name)
            {
                return corner
            }
        }
        return null
    }

    fun findCornerByPosition(corners : Array<Corner>, position : CornerPosition) : Corner? {
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