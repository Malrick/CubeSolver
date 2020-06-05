package app.model.cube.coordinates

import app.model.Color

class CubeCoordinates(var height : Int, var depht : Int, var width : Int) {

    fun getSideCoordinate(color : Color, cubeSize : Int) : SideCoordinate?
    {
        when(color)
        {
            Color.WHITE -> {
                if(height!=cubeSize-1) return null
                else return SideCoordinate(width, cubeSize-depht-1)
            }
            Color.YELLOW -> {
                if(height!=0) return null
                else return SideCoordinate(width, depht)
            }
            Color.ORANGE -> {
                if(width!=0) return null
                else return SideCoordinate(cubeSize-depht-1, cubeSize-height-1)
            }
            Color.RED -> {
                if(width!=cubeSize-1) return null
                else return SideCoordinate(depht, cubeSize-height-1)
            }
            Color.BLUE -> {
                if(depht!=cubeSize-1) return null
                else return SideCoordinate(width, height)
            }
            Color.GREEN -> {
                if(depht!=0) return null
                else return SideCoordinate(width, cubeSize-height-1)
            }
        }
    }

}