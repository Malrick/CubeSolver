package app.vision

import app.model.cubeUtils.Color
import app.vision.utils.ColorUtils
import app.vision.utils.GeometryUtils
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.*
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class ColorResolver : KoinComponent {

    val geometryUtils : GeometryUtils by inject()
    val colorUtils : ColorUtils by inject()

    val showPictures = false

    fun takePictureAndSave(pathName : String)
    {
        var videoCap = VideoCap()
        videoCap.openCam()
        val outputfile = File(pathName)
        ImageIO.write(videoCap.getOneFrame(), "jpg", outputfile)
    }

    fun initFixContours() : Array<MatOfPoint>
    {
        var toReturn = arrayOf<MatOfPoint>()
        var diffY = 10.0
        var diffX = - 25.0
        toReturn += (MatOfPoint(Point(162.0+diffX,11.0+diffY), Point(223.0+diffX,10.0+diffY), Point(224.0+diffX,67.0+diffY), Point(163.0+diffX,68.0+diffY)))
        toReturn += (MatOfPoint(Point(318.0+diffX,15.0+diffY), Point(371.0+diffX,14.0+diffY), Point(372.0+diffX,67.0+diffY), Point(319.0+diffX,68.0+diffY)))
        toReturn += (MatOfPoint(Point(444.0+diffX,25.0+diffY), Point(501.0+diffX,24.0+diffY), Point(502.0+diffX,79.0+diffY), Point(445.0+diffX,80.0+diffY)))
        toReturn += (MatOfPoint(Point(173.0+diffX,136.0+diffY), Point(232.0+diffX,137.0+diffY), Point(231.0+diffX,202.0+diffY), Point(172.0+diffX,201.0+diffY)))
        toReturn += (MatOfPoint(Point(302.0+diffX,141.0+diffY), Point(369.0+diffX,140.0+diffY), Point(370.0+diffX,207.0+diffY), Point(303.0+diffX,208.0+diffY)))
        toReturn += (MatOfPoint(Point(445.0+diffX,152.0+diffY), Point(508.0+diffX,153.0+diffY), Point(507.0+diffX,220.0+diffY), Point(444.0+diffX,219.0+diffY)))
        toReturn += (MatOfPoint(Point(163.0+diffX,270.0+diffY), Point(226.0+diffX,271.0+diffY), Point(225.0+diffX,342.0+diffY), Point(162.0+diffX,341.0+diffY)))
        toReturn += (MatOfPoint(Point(309.0+diffX,284.0+diffY), Point(370.0+diffX,285.0+diffY), Point(369.0+diffX,348.0+diffY), Point(308.0+diffX,347.0+diffY)))
        toReturn += (MatOfPoint(Point(448.0+diffX,293.0+diffY), Point(511.0+diffX,292.0+diffY), Point(512.0+diffX,355.0+diffY), Point(449.0+diffX,356.0+diffY)))
        return toReturn
    }

    /*
    Take picture
    Resolve contours of ROIs
    For each ROI
        Get dominant color
     */

    // Virer l'affichage de 10 000 photos
    // Init les contours mieux que ça
    // Détecter les couleurs en LAB ou en HSV

    fun resolveColors(whiteLab : Scalar, filename : String, takePicture : Boolean, color : Color) : Array<Color>
    {
        val contoursFinaux = initFixContours()

        var results = arrayOf<Color>()

        if(takePicture) takePictureAndSave(filename)

        var original = Imgcodecs.imread(filename, 1)
        var originalAvecContours = original.clone()
        var toExamine = original.clone()

        Imgproc.drawContours(originalAvecContours, contoursFinaux.toList(), -1, Scalar(0.0, 0.0, 0.0), -1)
        Photo.fastNlMeansDenoisingColored(toExamine, toExamine)

        println(filename)

        for(i in 0..8)
        {
            // INIT ROI
            // Est-ce qu'on peut pas faire la ROI plus rapidement ?
            var mask = Mat.zeros(toExamine.size(), CvType.CV_8UC1)
            Imgproc.drawContours(mask, contoursFinaux.toList(), i, Scalar(255.0), -1)
            var ROI = Mat()
            toExamine.copyTo(ROI, mask)

            var dominantColor = colorUtils.KnnClustering(ROI, 10)
            var labDominantColor = colorUtils.scalarBGR2Lab(dominantColor.`val`[2], dominantColor.`val`[1], dominantColor.`val`[0])
            var resolvedColor = colorUtils.resolveColorNN(whiteLab, labDominantColor)
            //var resolvedColor = colorUtils.resolveColor(dominantColor)
            results += resolvedColor

            // UI
            // On dessine le contour rempli de la couleur détectée
            Imgproc.drawContours(originalAvecContours, contoursFinaux.toList(), i, Scalar(dominantColor.`val`[2], dominantColor.`val`[1], dominantColor.`val`[0]),-1)
            // On entoure cette zone d'un contour parce que c'est plus visible
            Imgproc.drawContours(originalAvecContours, contoursFinaux.toList(), i, Scalar(0.0, 0.0, 0.0))
            HighGui.imshow(i.toString(), ROI)

            print(i.toString() +" : " + resolvedColor.name)
            println("  " + labDominantColor.`val`[0] + "  " + labDominantColor.`val`[1] + "  " + labDominantColor.`val`[2])

        }

        println()

        HighGui.imshow("Original avec contours", originalAvecContours)
        HighGui.imshow("Examiné", toExamine)
        HighGui.imshow("Original", original)

        if(showPictures) HighGui.waitKey(0)

        return results
    }

    fun processColorLabs() : Array<Scalar>
    {
        val contoursFinaux = initFixContours()

        takePictureAndSave("tempColors.jpg")

        var original = Imgcodecs.imread("tempColors.jpg", 1)
        var originalAvecContours = original.clone()

        var tempMeanL = arrayOf<Scalar>()

        for(i in 0..8)
        {
            // INIT ROI
            // Est-ce qu'on peut pas faire la ROI plus rapidement ?
            var mask = Mat.zeros(original.size(), CvType.CV_8UC1)
            Imgproc.drawContours(mask, contoursFinaux.toList(), i, Scalar(255.0), -1)
            var ROI = Mat()
            original.copyTo(ROI, mask)
            var dominantColor = colorUtils.KnnClustering(ROI, 10)

            // UI
            // On dessine le contour rempli de la couleur détectée
            Imgproc.drawContours(originalAvecContours, contoursFinaux.toList(), i, Scalar(dominantColor.`val`[2], dominantColor.`val`[1], dominantColor.`val`[0]),-1)
            // On entoure cette zone d'un contour parce que c'est plus visible
            Imgproc.drawContours(originalAvecContours, contoursFinaux.toList(), i, Scalar(0.0, 0.0, 0.0))

            tempMeanL += colorUtils.scalarBGR2Lab(dominantColor.`val`[2], dominantColor.`val`[1], dominantColor.`val`[0])
        }

        HighGui.imshow("Original", original)
        HighGui.imshow("Original avec contours", originalAvecContours)

        if(showPictures) HighGui.waitKey(0)

        return tempMeanL
    }

    fun normalizeLab(scalar : Scalar) : Scalar
    {
        return Scalar((scalar.`val`[0]/255.0) * 100, scalar.`val`[1]-128.0, scalar.`val`[2]-128.0)
    }


}