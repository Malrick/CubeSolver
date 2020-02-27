package app.model.cubeOLD

import app.model.cubeUtils.Color


class Side {

    fun clone() : Side
    {
        val clone = Side()
        clone.sideColor = sideColor
        for(i in 0..2)
        {
            var array = arrayOf<Color>()
            for(j in 0..2)
            {
                array+= colors[i][j]
            }
            clone.colors+=array
        }
        return clone
    }

    var colors = arrayOf<Array<Color>>()
    lateinit var sideColor : Color

}