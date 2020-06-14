package app.UI

import app.model.Color
import app.service.robot.RobotColorService
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.highgui.HighGui

class MatricesDisplay : KoinComponent {

    private val robotColorService : RobotColorService by inject()

    fun displayConcatenatedCube(colors : HashMap<Color, Array<Mat>>)
    {
        var concatenatedMats = listOf<Mat>()
        var superConcatenation = Mat()
        for(color in Color.values())
        {
            var mat = Mat()
            Core.hconcat(colors[color]?.asList(), mat)
            concatenatedMats += mat
        }
        Core.vconcat(concatenatedMats, superConcatenation)
        HighGui.imshow("couleurs concaténées", superConcatenation)

        var dominantColors = HashMap<Color, MutableList<Mat>>()
        concatenatedMats = listOf()
        superConcatenation = Mat()

        for ((sideColor, colors) in colors) {
            dominantColors[sideColor] = mutableListOf()
            for (elem in colors) {
                dominantColors[sideColor]?.plusAssign(elem.clone().setTo(robotColorService.getBgrDominantColor(elem)))
            }
        }

        for (color in Color.values()) {
            var mat = Mat()

            Core.hconcat(dominantColors[color], mat)
            concatenatedMats += mat
        }
        Core.vconcat(concatenatedMats, superConcatenation)
        HighGui.imshow("couleurs dominantes", superConcatenation)
        HighGui.waitKey(0)
    }
}