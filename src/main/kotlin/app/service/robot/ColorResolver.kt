package app.service.robot

import app.model.Color
import app.model.robot.vision.ColorProcessing
import app.utils.algorithms.clustering.KNN
import app.utils.vision.ColorSpaceUtils
import app.utils.vision.GeometryUtils
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.nd4j.linalg.factory.Nd4j
import org.opencv.core.*
import java.util.HashMap

/*
    Classifies colors detected by the robot
 */
class ColorResolver : KoinComponent {

    private val colorUtils : ColorSpaceUtils by inject()
    private val geometryUtils : GeometryUtils by inject()
    private val knn : KNN by inject()

    fun resolve(detectedColors : HashMap<Color, Array<Mat>>, method : ColorProcessing) : HashMap<Color, Array<Color>>
    {
        return when(method)
        {
            ColorProcessing.NeuralNetwork -> {
                queryNeuralNetwork(detectedColors)
            }
            ColorProcessing.ClosestDistance -> {
                resolveByClosestDistance(detectedColors)
            }
            ColorProcessing.Clustering -> {
                TODO()
            }
        }
    }

    // Get centers colors, then for each colored square get the closest center, assign its color to it
    private fun resolveByClosestDistance(detectedColors: HashMap<Color, Array<Mat>>) : HashMap<Color, Array<Color>>
    {
        var centerColors = HashMap<Color, Scalar>()
        var resolvedColor = HashMap<Color, Array<Color>>()

        for(color in Color.values())
        {
            resolvedColor[color] = Array(9) { Color.WHITE}
        }

        for((sideColor, colors) in detectedColors)
        {
            centerColors[sideColor] = colorUtils.scalarBGR2Lab(getBgrDominantColor(colors[4]))
        }

        for(color in detectedColors.keys)
        {
            for(i in 0 until 9)
            {
                var distances = HashMap<Color, Double>()
                for(color2 in Color.values())
                {
                    distances[color2] = geometryUtils.getDistance(colorUtils.scalarBGR2Lab(getBgrDominantColor(detectedColors[color]!![i])), centerColors[color2]!!)
                }
                resolvedColor[color]!![i] = distances.minBy { it.value }!!.key
            }
        }

        return resolvedColor
    }

    // Resolve Rubik's color by querying neural network for each color squares
    private fun queryNeuralNetwork(detectedColors: HashMap<Color, Array<Mat>>) : HashMap<Color, Array<Color>>
    {
        var whiteLab = getBgrDominantColor(detectedColors[Color.WHITE]!![4])
        var resolvedColors = HashMap<Color, Array<Color>>()

        for((sideColor, colors) in detectedColors)
        {
            var dominantColors = colorUtils.scalarsBGR2Lab(getBgrDominantColors(colors))
            var sideResolvedColors = arrayOf<Color>()

            for(color in dominantColors)
            {
                sideResolvedColors += queryNeuralNetwork(whiteLab, color)
            }
            resolvedColors[sideColor] = sideResolvedColors
        }

        return resolvedColors
    }

    // Query Keras model
    private fun queryNeuralNetwork(whiteLab : Scalar, DetectedColorLab : Scalar) : Color
    {
        var model = KerasModelImport.importKerasSequentialModelAndWeights("models/colorModel.h5")

        val array3d = arrayOf(arrayOf(
            floatArrayOf(whiteLab.`val`[0].toFloat(), whiteLab.`val`[1].toFloat(), whiteLab.`val`[2].toFloat(), DetectedColorLab.`val`[0].toFloat(), DetectedColorLab.`val`[1].toFloat(), DetectedColorLab.`val`[2].toFloat())
        ))
        val input = Nd4j.createFromArray(array3d)

        var output = model.output(input)

        var predictions = output.data().asFloat()

        var argmax = predictions.indices.maxBy { predictions[it] }

        return when(argmax)
        {
            0 -> Color.WHITE
            1 -> Color.ORANGE
            2 -> Color.GREEN
            3 -> Color.RED
            4 -> Color.YELLOW
            5 -> Color.BLUE
            else -> Color.WHITE
        }
    }

    // Get the dominant colors from matrices
    fun getBgrDominantColors(colors : Array<Mat>) : Array<Scalar>
    {
        var results = arrayOf<Scalar>()
        for(color in colors)
        {
            results += getBgrDominantColor(color)
        }
        return results
    }

    // Get the dominant color of a matrice
    fun getBgrDominantColor(color : Mat) : Scalar
    {
        return knn.KnnClustering(color, 20)
    }

}