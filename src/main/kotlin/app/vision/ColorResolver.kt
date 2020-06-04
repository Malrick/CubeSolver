package app.vision

import app.model.cubeUtils.Color
import app.model.vision.ColorProcessing
import app.vision.utils.ColorUtils
import it.unimi.dsi.fastutil.Hash
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.nd4j.linalg.factory.Nd4j
import org.opencv.core.*
import java.lang.Math.sqrt
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.pow

class ColorResolver : KoinComponent {

    private val colorUtils : ColorUtils by inject()

    fun resolve(detectedColors : HashMap<Color, Array<Mat>>, method : ColorProcessing) : HashMap<Color, Array<Color>>
    {
        var resolvedColors = HashMap<Color, Array<Color>>()

        when(method)
        {
            ColorProcessing.NeuralNetwork -> {
                for((sideColor, colors) in detectedColors)
                {
                    resolvedColors[sideColor] = resolveColorNN(detectedColors[Color.WHITE]?.get(4)!!, colors)
                }
            }
            ColorProcessing.ClosestDistance -> {
                resolvedColors = resolveClosestDistance(detectedColors)
            }
        }

        return resolvedColors
    }

    fun getDistance(a : Scalar, b : Scalar) : Double
    {
        return sqrt((a.`val`[0] - b.`val`[0]).pow(2) + (a.`val`[1] - b.`val`[1]).pow(2) + (a.`val`[2] - b.`val`[2]).pow(2) )
    }

    fun resolveClosestDistance(detectedColors: HashMap<Color, Array<Mat>>) : HashMap<Color, Array<Color>>
    {
        var centerColors = HashMap<Color, Scalar>()
        var resolvedColor = HashMap<Color, Array<Color>>()

        for(color in Color.values())
        {
            resolvedColor[color] = Array(9) { Color.WHITE}
        }

        for((sideColor, colors) in detectedColors)
        {
            centerColors[sideColor] = colorUtils.scalarBGR2Lab(KnnClustering(colors[4], 30))
        }

        for(color in detectedColors.keys)
        {
            for(i in 0 until 9)
            {
                var distances = HashMap<Color, Double>()
                for(color2 in Color.values())
                {
                    distances[color2] = getDistance(colorUtils.scalarBGR2Lab(KnnClustering(detectedColors[color]!![i], 30)), centerColors[color2]!!)
                }
                resolvedColor[color]!![i] = distances.minBy { it.value }!!.key
            }
        }

        return resolvedColor
    }


    fun resolveColorNN(whiteMat : Mat, detectedColors: Array<Mat>) : Array<Color>
    {
        var returnedColors = arrayOf<Color>()
        var whiteLab = colorUtils.scalarBGR2Lab(KnnClustering(whiteMat, 10))
        for(mat in detectedColors)
        {
            returnedColors += resolveColorNN(whiteLab, colorUtils.scalarBGR2Lab(KnnClustering(mat, 10)))
        }
        return returnedColors
    }

    fun resolveColorNN(whiteLab : Scalar, DetectedColorLab : Scalar) : Color
    {
        var model = KerasModelImport.importKerasSequentialModelAndWeights("models/colorModel.h5")

        val array3d = arrayOf(arrayOf(
            floatArrayOf(whiteLab.`val`[0].toFloat(), whiteLab.`val`[1].toFloat(), whiteLab.`val`[2].toFloat(), DetectedColorLab.`val`[0].toFloat(), DetectedColorLab.`val`[1].toFloat(), DetectedColorLab.`val`[2].toFloat())
        ))
        val input = Nd4j.createFromArray(array3d)

        var output = model.output(input)

        var argmax = 0

        for(i in output.data().asFloat().indices)
        {
            if(output.data().asFloat()[i] > output.data().asFloat()[argmax])
            {
                argmax = i
            }
        }

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

    // Boite noire
    // TODO : blanchir
    fun KnnClustering(cutout: Mat, k: Int): Scalar {
        val samples = cutout.reshape(1, cutout.cols() * cutout.rows())
        val samples32f = Mat()
        samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0)
        val labels = Mat()
        val criteria = TermCriteria(TermCriteria.COUNT, 100, 1.0)
        val centers = Mat()
        Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers)
        centers.convertTo(centers, CvType.CV_8UC1, 255.0)
        centers.reshape(3)

        val clusters= ArrayList<Mat>()
        for (i in 0 until centers.rows()) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()))
        }
        val counts: MutableMap<Int, Int> = HashMap()
        for (i in 0 until centers.rows()) counts[i] = 0
        var rows = 0
        for (y in 0 until cutout.rows()) {
            for (x in 0 until cutout.cols()) {
                val label = labels[rows, 0][0].toInt()
                val r = centers[label, 2][0].toInt()
                val g = centers[label, 1][0].toInt()
                val b = centers[label, 0][0].toInt()
                counts[label] = counts[label]!! + 1
                clusters[label].put(y, x, b.toDouble(), g.toDouble(), r.toDouble())
                rows++
            }
        }
        for(i in 0 until centers.rows())
        {
            var r = centers[i, 0][0]
            var g = centers[i, 1][0]
            var b = centers[i, 2][0]
        }

        var maxCountIndex = counts.filter { it != counts.maxBy { it.value } }.maxBy { it.value }!!.key


        return Scalar(centers[maxCountIndex, 0][0], centers[maxCountIndex, 1][0], centers[maxCountIndex, 2][0])
    }


}