package app.vision.utils

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.CvType
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import java.util.ArrayList

class PictureProcessingUtils : KoinComponent{

    val geometryUtils : GeometryUtils by inject()

    private fun polygonApproximation(contours : ArrayList<MatOfPoint>) : ArrayList<MatOfPoint> {
        var iteratorContours: Iterator<MatOfPoint> = contours.iterator()
        var approximations = ArrayList<MatOfPoint>()

        while (iteratorContours.hasNext()) {
            val contour = iteratorContours.next()

            val matOfPoint = MatOfPoint(contour)
            var approx = MatOfPoint2f()
            matOfPoint.convertTo(approx, CvType.CV_32FC2)
            val epsilon = 0.09 * Imgproc.arcLength(approx, true)

            Imgproc.approxPolyDP(approx, approx, epsilon, true)

            approx.convertTo(matOfPoint, CvType.CV_32S)

            approximations.add(matOfPoint)
        }
        return approximations
    }

    fun filterAndSortApproximations(approximations : ArrayList<MatOfPoint>) : ArrayList<MatOfPoint>
    {
        var toReturn = approximations.filter{ it.toList().size == 4 } as ArrayList<MatOfPoint>
        //approximations = approximations.filter{ Imgproc.isContourConvex(it) } as ArrayList<MatOfPoint>

        toReturn = approximations.filter{ geometryUtils.isApproximativelyASquareByFourPoints(listOf(Point(it[0,0][0], it[0,0][1]), Point(it[1,0][0], it[1,0][1]), Point(it[2,0][0], it[2,0][1]), Point(it[3,0][0], it[3,0][1]))) } as ArrayList<MatOfPoint>
        toReturn.sortByDescending { Imgproc.contourArea(it) }

        var toRemove = ArrayList<MatOfPoint>()
        var notToRemove = ArrayList<MatOfPoint>()

        for(elem in toReturn.toList())
        {
            for(elem2 in toReturn.toList())
            {
                if(elem != elem2)
                {
                    if(geometryUtils.isSquareParent(listOf(Point(elem[0,0][0], elem[0,0][1]), Point(elem[1,0][0], elem[1,0][1]), Point(elem[2,0][0], elem[2,0][1]), Point(elem[3,0][0], elem[3,0][1])), listOf(
                            Point(elem2[0,0][0], elem2[0,0][1]), Point(elem2[1,0][0], elem2[1,0][1]), Point(elem2[2,0][0], elem2[2,0][1]), Point(elem2[3,0][0], elem2[3,0][1])
                        )))
                    {
                        toRemove.add(elem)
                        notToRemove.add(elem2)
                    }
                }
            }

        }

        toReturn.removeAll(toRemove)
        toReturn.addAll(notToRemove)

        return toReturn
    }

    private fun sortContours(contoursFinaux : ArrayList<MatOfPoint>) {
        contoursFinaux.sortBy { Imgproc.boundingRect(it).y }
        var subTri1 =
            listOf(contoursFinaux[0], contoursFinaux[1], contoursFinaux[2]).sortedBy { Imgproc.boundingRect(it).x }
        var subTri2 =
            listOf(contoursFinaux[3], contoursFinaux[4], contoursFinaux[5]).sortedBy { Imgproc.boundingRect(it).x }
        var subTri3 =
            listOf(contoursFinaux[6], contoursFinaux[7], contoursFinaux[8]).sortedBy { Imgproc.boundingRect(it).x }

        contoursFinaux.clear()

        contoursFinaux.addAll(subTri1)
        contoursFinaux.addAll(subTri2)
        contoursFinaux.addAll(subTri3)

        var i = 0
        for(elem in contoursFinaux.toList())
        {
            println(i)
            i++
            println("(" + elem[0,0][0].toInt().toString() + "," + elem[0,0][1].toInt().toString()+")")
            println("(" + elem[1,0][0].toInt().toString() + "," + elem[1,0][1].toInt().toString()+")")
            println("(" + elem[2,0][0].toInt().toString() + "," + elem[2,0][1].toInt().toString()+")")
            println("(" + elem[3,0][0].toInt().toString() + "," + elem[3,0][1].toInt().toString()+")")
            println()
        }
    }

}