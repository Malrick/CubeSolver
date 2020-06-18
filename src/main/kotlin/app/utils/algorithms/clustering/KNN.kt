package app.utils.algorithms.clustering

import org.opencv.core.*
import java.util.ArrayList
import java.util.HashMap

class KNN {

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