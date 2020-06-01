package app.vision.utils

import app.model.cubeUtils.Color
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.nd4j.linalg.factory.Nd4j
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.util.ArrayList
import java.util.HashMap


class ColorUtils : KoinComponent {

    fun scalarBGR2Lab(B : Double, G : Double, R : Double) : Scalar
    {
        var bgr = Mat(1,1, CvType.CV_8UC3, Scalar(B,G,R))
        var lab = Mat()
        Imgproc.cvtColor(bgr, lab, Imgproc.COLOR_BGR2Lab)
        return Scalar(lab[0,0][0]/255.0*100.0, lab[0,0][1]-128.0, lab[0,0][2]-128.0)
    }

    fun resolveColorNN(WhiteColorLab : Scalar, DetectedColorLab : Scalar) : Color
    {
        var model = KerasModelImport.importKerasSequentialModelAndWeights("models/colorModel.h5")

        val array3d = arrayOf(arrayOf(
            floatArrayOf(WhiteColorLab.`val`[0].toFloat(), WhiteColorLab.`val`[1].toFloat(), WhiteColorLab.`val`[2].toFloat(), DetectedColorLab.`val`[0].toFloat(), DetectedColorLab.`val`[1].toFloat(), DetectedColorLab.`val`[2].toFloat())
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