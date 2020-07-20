package app.model.cube.coordinates

import app.model.Color
import app.model.cube.Cube
import app.model.orientation.RelativePosition


/*
    Defines the coordinate on a piece on the cube, from the cube point of view. (x,y,z)
    It is possible to be getting the coordinate of a piece on a certain side
 */
class CubeCoordinates(var height : Int, var depht : Int, var width : Int) {

    fun getSideCoordinate(color : Color, cube : Cube) : SideCoordinate?
    {
        when(color)
        {
            cube.orientation.colorPositions[RelativePosition.TOP] -> {
                if(height!=cube.cubeSize-1) return null
                else return SideCoordinate(width, cube.cubeSize-depht-1)
            }
            cube.orientation.colorPositions[RelativePosition.BOTTOM] -> {
                if(height!=0) return null
                else return SideCoordinate(width, depht)
            }
            cube.orientation.colorPositions[RelativePosition.LEFT] -> {
                if(width!=0) return null
                else return SideCoordinate(cube.cubeSize-depht-1, cube.cubeSize-height-1)
            }
            cube.orientation.colorPositions[RelativePosition.RIGHT] -> {
                if(width!=cube.cubeSize-1) return null
                else return SideCoordinate(depht, cube.cubeSize-height-1)
            }
            cube.orientation.colorPositions[RelativePosition.BACK] -> {
                if(depht!=cube.cubeSize-1) return null
                else return SideCoordinate(width, height)
            }
            cube.orientation.colorPositions[RelativePosition.FRONT] -> {
                if(depht!=0) return null
                else return SideCoordinate(width, cube.cubeSize-height-1)
            }
        }
        return null
    }

}