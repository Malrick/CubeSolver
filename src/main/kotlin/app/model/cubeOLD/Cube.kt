package app.model.cubeOLD

import app.helper.InitHelper
import app.model.cubeUtils.Color


class Cube {

    fun clone() : Cube
    {
        val clone = Cube()
        for(color in Color.values())
        {
            clone.sides[color] = sides[color]!!.clone()
        }
        //clone.corners = InitHelper().initCorner(clone.sides)
        //clone.edges = InitHelper().initEdge(clone.sides)
        return clone
    }

    var sides : HashMap<Color, Side> = HashMap()
    var corners = arrayOf<Corner>()
    var edges = arrayOf<Edge>()

}