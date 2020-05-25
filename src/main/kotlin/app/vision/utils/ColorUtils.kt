package app.vision.utils

import app.model.cubeUtils.Color
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.util.ArrayList
import java.util.HashMap

class ColorUtils : KoinComponent {

    val geometryUtils : GeometryUtils by inject()

    var colorLabs = HashMap<Color, Scalar>()

    init{
        colorLabs[Color.WHITE] = Scalar(70.0, -5.0, 15.0 )
        colorLabs[Color.ORANGE] = Scalar(65.0 , 55.0 , 45.0)
        colorLabs[Color.GREEN] = Scalar(50.0, -55.0 , 40.0 )
        colorLabs[Color.RED] = Scalar(45.0 , 50.00 , 30.0 )
        colorLabs[Color.YELLOW] = Scalar(60.0,-25.0, 40.0)
        colorLabs[Color.BLUE] = Scalar(50.0,-30.0,-30.0)
    }

    fun resolveColorsByPoints(color : Scalar, points : HashMap<Color, Array<Scalar>>) : Color
    {

        var closestPointDistanceWhite = points[Color.WHITE]!!.minBy { geometryUtils.getDistance(color, it) }
        var closestPointDistanceOrange = points[Color.ORANGE]!!.minBy { geometryUtils.getDistance(color, it) }
        var closestPointDistanceGreen = points[Color.GREEN]!!.minBy { geometryUtils.getDistance(color, it) }
        var closestPointDistanceRed = points[Color.RED]!!.minBy { geometryUtils.getDistance(color, it) }
        var closestPointDistanceYellow = points[Color.YELLOW]!!.minBy { geometryUtils.getDistance(color, it) }
        var closestPointDistanceBlue = points[Color.BLUE]!!.minBy { geometryUtils.getDistance(color, it) }

        return when(arrayOf(geometryUtils.getDistance(closestPointDistanceWhite!!, color), geometryUtils.getDistance(closestPointDistanceOrange!!, color), geometryUtils.getDistance(closestPointDistanceGreen!!, color), geometryUtils.getDistance(closestPointDistanceRed!!, color), geometryUtils.getDistance(closestPointDistanceYellow!!, color), geometryUtils.getDistance(closestPointDistanceBlue!!, color)).min())
        {
            geometryUtils.getDistance(closestPointDistanceWhite, color) -> Color.WHITE
            geometryUtils.getDistance(closestPointDistanceOrange, color) -> Color.ORANGE
            geometryUtils.getDistance(closestPointDistanceGreen, color) -> Color.GREEN
            geometryUtils.getDistance(closestPointDistanceRed, color) -> Color.RED
            geometryUtils.getDistance(closestPointDistanceYellow, color) -> Color.YELLOW
            geometryUtils.getDistance(closestPointDistanceBlue, color) -> Color.BLUE
            else -> Color.BLUE
        }
    }

    fun scalarBGR2Lab(B : Double, G : Double, R : Double) : Scalar
    {
        var bgr = Mat(1,1, CvType.CV_8UC3, Scalar(B,G,R))
        var lab = Mat()
        Imgproc.cvtColor(bgr, lab, Imgproc.COLOR_BGR2Lab)
        return Scalar(lab[0,0][0]/255.0*100.0, lab[0,0][1]-128.0, lab[0,0][2]-128.0)
    }

    // Partira avec le NN
    fun resolveColor(dominantColor: Scalar) : Color {

        var labDominantColor = scalarBGR2Lab(dominantColor.`val`[2], dominantColor.`val`[1], dominantColor.`val`[0])

        var similarities = HashMap<Color, Double>()

        for((color, lab) in colorLabs)
        {
            similarities[color] = geometryUtils.getDistance(labDominantColor, lab)
        }

        return similarities.minBy { it.value }!!.key
    }

    // Boite noire
    fun KnnClustering(cutout: Mat, k: Int): Scalar{
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
            var r = centers[i, 2][0]
            var g = centers[i, 1][0]
            var b = centers[i, 0][0]
        }

        var maxCountIndex = counts.filter { it != counts.maxBy { it.value } }.maxBy { it.value }!!.key


        return Scalar(centers[maxCountIndex, 2][0], centers[maxCountIndex, 1][0], centers[maxCountIndex, 0][0])
    }


}