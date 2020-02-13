package app.opticRecognition

import app.model.constants.Color
import org.opencv.core.*
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo
import java.io.File
import java.lang.Math.*
import java.util.*
import javax.imageio.ImageIO

class ColorResolver {

    var imageGrise = Mat()
    var imageFloue = Mat()
    var contoursDepuisFlou = Mat()
    var imageDilatee = Mat()

    var contours = ArrayList<MatOfPoint>()
    var approximations = ArrayList<MatOfPoint>()
    var contoursFinaux = ArrayList<MatOfPoint>()

    var orangeLab = Scalar(60.0 , 50.0 , 50.0)
    var redLab = Scalar(30.0 , 50.00 , 50.0 )
    var greenLab = Scalar(50.0, -60.0 , 60.0 )
    var blueLab = Scalar(50.0,-40.0,-70.0)
    var yellowLab = Scalar(60.0,-15.49, 60.0)
    var whiteLab = Scalar(100.0, 0.0, 0.0 )

    var results = ArrayList<Color>()

    var rand = Random()

    fun takePictureAndSave(pathName : String)
    {
        var videoCap = VideoCap()
        videoCap.openCam()
        val outputfile = File(pathName)
        ImageIO.write(videoCap.getOneFrame(), "jpg", outputfile)
    }

    fun resolveColors(filename : String) : ArrayList<Color>
    {
        results.clear()
        contours.clear()
        approximations.clear()
        contoursFinaux.clear()
        //takePictureAndSave(filename)

        var original = Imgcodecs.imread(filename, 1)
        var imageDilatee = Mat()
        Photo.fastNlMeansDenoisingColored(original, original)
        Imgproc.cvtColor(original, imageDilatee, Imgproc.COLOR_BGR2GRAY)
        Imgproc.blur(imageDilatee, imageFloue, Size(6.0, 6.0))
        Imgproc.Canny(imageFloue, contoursDepuisFlou, 15.0, 15.0, 3)

        var dilateSize = 3.0

        var element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, Size(2 * dilateSize + 1, 2 * dilateSize + 1), Point(dilateSize, dilateSize))

        Imgproc.dilate(contoursDepuisFlou, imageDilatee, element)

        Imgproc.findContours(imageDilatee, contours, Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        polygonApproximation()

        contours.clear()

        contours.addAll(approximations)

        var imgApprox = original.clone()

        for(i in 0..contours.size-1)
        {
            Imgproc.drawContours(imgApprox, contours, i, Scalar(rand.nextInt(255).toDouble(), rand.nextInt(255).toDouble(), rand.nextInt(255).toDouble()))
        }

        filterAndSortApproximations()

        for(i in 0..8)
        {
            contoursFinaux.add(approximations[i])
        }

        sortContours()

        var originalAvecContours = original.clone()
        var toExamine = original.clone()

        Imgproc.drawContours(originalAvecContours, contoursFinaux, -1, Scalar(0.0, 0.0, 0.0), -1)

        Photo.fastNlMeansDenoisingColored(toExamine, toExamine)

        println(filename)
        for(i in 0..8)
        {
            // INIT ROI
            var mask = Mat.zeros(toExamine.size(), CvType.CV_8UC1)
            Imgproc.drawContours(mask, contoursFinaux, i, Scalar(255.0), -1)
            var ROI = Mat()
            toExamine.copyTo(ROI, mask)

            var dominantColor = cluster(ROI, 10)

            // UI
            Imgproc.drawContours(originalAvecContours, contoursFinaux, i, Scalar(dominantColor[2], dominantColor[1], dominantColor[0]),-1)
            Imgproc.drawContours(originalAvecContours, contoursFinaux, i, Scalar(0.0, 0.0, 0.0))
            print(i.toString() +" : ")

            solveAndPrintColor(dominantColor)

            HighGui.imshow(i.toString(), ROI)
        }
        println()

        HighGui.imshow("Original avec contours", originalAvecContours)
        HighGui.imshow("Approximations", imgApprox)
        HighGui.imshow("Contours depuis flou", contoursDepuisFlou)
        HighGui.imshow("Image floue", imageFloue)
        HighGui.imshow("Image dilatée", imageDilatee)
        HighGui.imshow("Original", original)
        HighGui.imshow("Examiné", toExamine)

        HighGui.waitKey(0)

        return results
    }

    private fun solveAndPrintColor(dominantColor: List<Double>) {
        var labDominantColor = ScalarBGR2LAB(dominantColor[2], dominantColor[1], dominantColor[0])


        var similarityOrange = getSimilarity(labDominantColor, orangeLab)
        var similarityRed = getSimilarity(labDominantColor, redLab)
        var similarityGreen = getSimilarity(labDominantColor, greenLab)
        var similarityBlue = getSimilarity(labDominantColor, blueLab)
        var similarityYellow = getSimilarity(labDominantColor, yellowLab)
        var similarityWhite = getSimilarity(labDominantColor, whiteLab)

        var min = arrayOf(
            similarityOrange,
            similarityRed,
            similarityGreen,
            similarityBlue,
            similarityYellow,
            similarityWhite
        ).min()

        if (min == similarityOrange){
            print("ORANGE")
            results.add(Color.ORANGE)
        }
        if (min == similarityRed) {
            print("ROUGE")
            results.add(Color.RED)
        }
        if (min == similarityGreen){
            print("VERT")
            results.add(Color.GREEN)
        }
        if (min == similarityBlue) {
            print("BLEU")
            results.add(Color.BLUE)
        }
        if (min == similarityYellow){
            print("JAUNE")
            results.add(Color.YELLOW)
        }
        if (min == similarityWhite) {
            print("BLANC")
            results.add(Color.WHITE)
        }

        println("  " + dominantColor[0] + "  " + dominantColor[1] + "  " + dominantColor[2])

        var maxDist = getSimilarity(Scalar(0.0, 0.0, 0.0), Scalar(255.0, 255.0, 255.0))

/*
                    println("Orange similarity : " + ((1.0-(similarityOrange/maxDist))*100).toInt().toString()+"%")
                    println("Red similarity : " + ((1.0-(similarityRed/maxDist))*100).toInt().toString()+"%")
                    println("Green similarity : " + ((1.0-(similarityGreen/maxDist))*100).toInt().toString()+"%")
                    println("Blue similarity : " + ((1.0-(similarityBlue/maxDist))*100).toInt().toString()+"%")
                    println("Yellow similarity : " + ((1.0-(similarityYellow/maxDist))*100).toInt().toString()+"%")
                    println("White similarity : " + ((1.0-(similarityWhite/maxDist))*100).toInt().toString()+"%")
                    println()*/

    }

    private fun sortContours() {
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
    }

    fun getDistance(a : Point, b : Point) : Double
    {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
    }

    fun resolveAngleByThreePoints(a : Point, b : Point, c : Point) : Double
    {
        return acos((pow(getDistance(a, b), 2.0) + pow(getDistance(a, c), 2.0) - pow(getDistance(b, c), 2.0)) / (2 * getDistance(a, b) * getDistance(a, c)))
    }

    fun isApproximativelyASquareByFourPoints(points : List<Point>) : Boolean
    {
        var distance01 = getDistance(points[0], points[1])
        var distance12 = getDistance(points[1], points[2])
        var distance23 = getDistance(points[2], points[3])
        var distance30 = getDistance(points[3], points[0])

        var averageDist = (distance01 + distance12 + distance23 + distance30)/4



        var acceptanceCriteria = 0.2

        //if(averageDist < (1 - acceptanceCriteria) * 65 || averageDist > (1 + acceptanceCriteria) * 65) return false

        if(distance12 < (1 - acceptanceCriteria) * averageDist || distance12 > (1 + acceptanceCriteria) * averageDist) return false
        if(distance23 < (1 - acceptanceCriteria) * averageDist || distance23 > (1 + acceptanceCriteria) * averageDist) return false
        if(distance30 < (1 - acceptanceCriteria) * averageDist || distance30 > (1 + acceptanceCriteria) * averageDist) return false

        var angle1 = resolveAngleByThreePoints(points[0], points[3], points[1])
        var angle2 = resolveAngleByThreePoints(points[1], points[0], points[2])
        var angle3 = resolveAngleByThreePoints(points[2], points[1], points[3])
        var angle4 = resolveAngleByThreePoints(points[3], points[2], points[0])

        if(angle1 < PI/2 * (1 - acceptanceCriteria) || angle1 > PI/2 * (1 + acceptanceCriteria)) return false
        if(angle2 < PI/2 * (1 - acceptanceCriteria) || angle2 > PI/2 * (1 + acceptanceCriteria)) return false
        if(angle3 < PI/2 * (1 - acceptanceCriteria) || angle3 > PI/2 * (1 + acceptanceCriteria)) return false
        if(angle4 < PI/2 * (1 - acceptanceCriteria) || angle4 > PI/2 * (1 + acceptanceCriteria)) return false

        return true
    }

    fun filterAndSortApproximations()
    {
        //approximations = approximations.filter{ Imgproc.isContourConvex(it) } as ArrayList<MatOfPoint>
        approximations = approximations.filter{ it.toList().size == 4 } as ArrayList<MatOfPoint>

        approximations = approximations.filter{ isApproximativelyASquareByFourPoints(listOf(Point(it[0,0][0], it[0,0][1]), Point(it[1,0][0], it[1,0][1]), Point(it[2,0][0], it[2,0][1]), Point(it[3,0][0], it[3,0][1]))) } as ArrayList<MatOfPoint>
        approximations.sortByDescending { Imgproc.contourArea(it) }

       var toRemove = ArrayList<MatOfPoint>()
        var notToRemove = ArrayList<MatOfPoint>()

        for(elem in approximations.toList())
        {
            for(elem2 in approximations.toList())
            {
                if(elem != elem2)
                {
                    if(isSquareParent(listOf(Point(elem[0,0][0], elem[0,0][1]), Point(elem[1,0][0], elem[1,0][1]), Point(elem[2,0][0], elem[2,0][1]), Point(elem[3,0][0], elem[3,0][1])), listOf(Point(elem2[0,0][0], elem2[0,0][1]), Point(elem2[1,0][0], elem2[1,0][1]), Point(elem2[2,0][0], elem2[2,0][1]), Point(elem2[3,0][0], elem2[3,0][1]))))
                    {
                        toRemove.add(elem)
                        notToRemove.add(elem2)
                    }
                }
            }

        }

        approximations.removeAll(toRemove)
        approximations.addAll(notToRemove)
/*
        for(elem in approximations.toList())
        {
            println("(" + elem[0,0][0].toInt().toString() + "," + elem[0,0][1].toInt().toString()+")")
            println("(" + elem[1,0][0].toInt().toString() + "," + elem[1,0][1].toInt().toString()+")")
            println("(" + elem[2,0][0].toInt().toString() + "," + elem[2,0][1].toInt().toString()+")")
            println("(" + elem[3,0][0].toInt().toString() + "," + elem[3,0][1].toInt().toString()+")")
            println()
        }*/
    }

    private fun polygonApproximation() {
        var iteratorContours: Iterator<MatOfPoint> = contours.iterator()

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

    }

    fun isSquareParent(a : List<Point>, b : List<Point>) : Boolean
    {
        var centerA = Point(a[0].x + (getDistance(a[0], a[1])/2) , a[0].y + (getDistance(a[0], a[1])/2))
        var centerB = Point(b[0].x + (getDistance(b[0], b[1])/2) , b[0].y + (getDistance(b[0], b[1])/2))

        var critere = 0.1

        if(centerA.x < ((1-critere) * centerB.x) || centerA.x > (1+critere)*centerB.x) return false
        if(centerA.y < ((1-critere) * centerB.y) || centerA.y > (1+critere)*centerB.y) return false
        if(getDistance(a[0], a[1]) < getDistance(b[0], b[1])) return false

        return true
    }


    fun getSimilarity(a : Scalar, b : Scalar) : Double
    {
        return kotlin.math.sqrt(((a.`val`[0] - b.`val`[0]) * (a.`val`[0] - b.`val`[0])) + ((a.`val`[1] - b.`val`[1]) * (a.`val`[1] - b.`val`[1])) + ((a.`val`[2] - b.`val`[2]) * (a.`val`[2] - b.`val`[2])))
    }

    fun ScalarBGR2LAB(B : Double, G : Double, R : Double) : Scalar
    {
        var bgr = Mat(1,1, CvType.CV_8UC3, Scalar(B,G,R))
        var lab = Mat()
        Imgproc.cvtColor(bgr, lab, Imgproc.COLOR_BGR2Lab)
        return Scalar(lab[0,0][0]/255.0*100.0, lab[0,0][1]-128.0, lab[0,0][2]-128.0)
    }


    fun cluster(cutout: Mat, k: Int): List<Double>{
        val samples = cutout.reshape(1, cutout.cols() * cutout.rows())
        val samples32f = Mat()
        samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0)
        val labels = Mat()
        val criteria = TermCriteria(TermCriteria.COUNT, 100, 1.0)
        val centers = Mat()
        Core.kmeans(samples32f, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers)
        return showClusters(cutout, labels, centers)
    }

    fun showClusters(cutout: Mat, labels: Mat, centers: Mat) : List<Double>{
        centers.convertTo(centers, CvType.CV_8UC1, 255.0)
        centers.reshape(3)

        val clusters: MutableList<Mat> = ArrayList()
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
        //println(counts)
        for(i in 0 until centers.rows())
        {
            var r = centers[i, 2][0]
            var g = centers[i, 1][0]
            var b = centers[i, 0][0]

            //println("$i : $r $g $b")
        }

        var toReturn = ArrayList<Double>()
        var maxCountIndex = counts.filter { it != counts.maxBy { it.value } }.maxBy { it.value }!!.key

        toReturn.add(centers[maxCountIndex, 2][0])
        toReturn.add(centers[maxCountIndex, 1][0])
        toReturn.add(centers[maxCountIndex, 0][0])

        //println("Choosen : " + maxCountIndex.toString())
        return toReturn
    }
}