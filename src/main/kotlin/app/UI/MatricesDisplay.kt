package app.UI

import app.model.Color
import app.service.robot.ColorService
import org.koin.core.KoinComponent
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.highgui.HighGui

class MatricesDisplay : KoinComponent {

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
        var colorResolver = ColorService()

        for ((sideColor, colors) in colors) {
            dominantColors[sideColor] = mutableListOf()
            for (elem in colors) {
                dominantColors[sideColor]?.plusAssign(elem.clone().setTo(colorResolver.KnnClustering(elem, 30)))
            }

            for (color in Color.values()) {
                var mat = Mat()

                Core.hconcat(dominantColors[color], mat)
                concatenatedMats += mat
            }
            Core.vconcat(concatenatedMats, superConcatenation)
            HighGui.imshow("couleurs dominantes", superConcatenation)
        }

        HighGui.waitKey(0)
    }
}