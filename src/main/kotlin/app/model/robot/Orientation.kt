package app.model.robot

import app.model.cubeUtils.Color
import app.model.robot.constants.PositionOfCubeEnum

class Orientation {

    var orientation = HashMap<PositionOfCubeEnum, Color>()

    fun init()
    {
        orientation[PositionOfCubeEnum.TOP] = Color.WHITE
        orientation[PositionOfCubeEnum.LEFT] = Color.ORANGE
        orientation[PositionOfCubeEnum.FRONT] = Color.GREEN
        orientation[PositionOfCubeEnum.RIGHT] = Color.RED
        orientation[PositionOfCubeEnum.BOTTOM] = Color.YELLOW
        orientation[PositionOfCubeEnum.BACK] = Color.BLUE
    }

    fun turnCube(positionOfCubeEnum: PositionOfCubeEnum)
    {
        if(positionOfCubeEnum == PositionOfCubeEnum.RIGHT)
        {
            var temp = orientation[PositionOfCubeEnum.FRONT]
            orientation[PositionOfCubeEnum.FRONT] = orientation[PositionOfCubeEnum.LEFT]!!
            orientation[PositionOfCubeEnum.LEFT] = orientation[PositionOfCubeEnum.BACK]!!
            orientation[PositionOfCubeEnum.BACK] = orientation[PositionOfCubeEnum.RIGHT]!!
            orientation[PositionOfCubeEnum.RIGHT] = temp!!
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.LEFT)
        {
            var temp = orientation[PositionOfCubeEnum.FRONT]
            orientation[PositionOfCubeEnum.FRONT] = orientation[PositionOfCubeEnum.RIGHT]!!
            orientation[PositionOfCubeEnum.RIGHT ] = orientation[PositionOfCubeEnum.BACK]!!
            orientation[PositionOfCubeEnum.BACK] = orientation[PositionOfCubeEnum.LEFT]!!
            orientation[PositionOfCubeEnum.LEFT] = temp!!
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.TOP)
        {
            var temp = orientation[PositionOfCubeEnum.FRONT]
            orientation[PositionOfCubeEnum.FRONT] = orientation[PositionOfCubeEnum.BOTTOM]!!
            orientation[PositionOfCubeEnum.BOTTOM ] = orientation[PositionOfCubeEnum.BACK]!!
            orientation[PositionOfCubeEnum.BACK] = orientation[PositionOfCubeEnum.TOP]!!
            orientation[PositionOfCubeEnum.TOP] = temp!!
        }
        else if(positionOfCubeEnum == PositionOfCubeEnum.BOTTOM)
        {
            var temp = orientation[PositionOfCubeEnum.FRONT]
            orientation[PositionOfCubeEnum.FRONT] = orientation[PositionOfCubeEnum.TOP]!!
            orientation[PositionOfCubeEnum.TOP ] = orientation[PositionOfCubeEnum.BACK]!!
            orientation[PositionOfCubeEnum.BACK] = orientation[PositionOfCubeEnum.BOTTOM]!!
            orientation[PositionOfCubeEnum.BOTTOM] = temp!!
        }
    }

    fun getPositionOfColor(color : Color) : PositionOfCubeEnum
    {
        return orientation.filter { it.value == color }.keys.first()
    }

}