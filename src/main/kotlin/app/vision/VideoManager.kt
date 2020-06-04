package app.vision

import app.vision.utils.ColorUtils
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.*
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo
import org.opencv.videoio.VideoCapture
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class VideoManager : KoinComponent {

    private val cam = VideoCapture()

    private val showPictures = false

    fun closeCam()
    {
        if(cam.isOpened)
        {
            cam.release()
        }
    }

    fun takePictureAndSave(fileName: String)
    {
        var mat2Image = Mat2Image()
        var myPicture : BufferedImage

        if(!cam.isOpened)
        {
            cam.open(0)
        }

        cam.read(mat2Image.mat)
        myPicture = mat2Image.getImage(mat2Image.mat)!!
        ImageIO.write(myPicture, "jpg",  File(fileName))
    }

    fun getContours() : Array<MatOfPoint>
    {
        var squareSize = 70
        var squareSpacing = 65

        var topLeftSquareCenterCoordinates = Scalar(180.0, 40.0)

        var squareCenters = arrayOf<Scalar>()
        var squareContours = arrayOf<MatOfPoint>()

        for(j in 0 until 3)
        {
            for(i in 0 until 3)
            {
                squareCenters += Scalar(topLeftSquareCenterCoordinates.`val`[0] + i * (squareSpacing + squareSize), topLeftSquareCenterCoordinates.`val`[1] + j * (squareSpacing + squareSize))
            }
        }

        for(elem in squareCenters)
        {
            squareContours += MatOfPoint(Point(elem.`val`[0]-(squareSize/2), elem.`val`[1]-(squareSize/2))
                                        ,Point(elem.`val`[0]-(squareSize/2), elem.`val`[1]+(squareSize/2))
                                        ,Point(elem.`val`[0]+(squareSize/2), elem.`val`[1]+(squareSize/2))
                                        ,Point(elem.`val`[0]+(squareSize/2), elem.`val`[1]-(squareSize/2)))
        }

        return squareContours
    }

    fun getROIs(picture : Mat, contours : Array<MatOfPoint>) : Array<Mat>
    {
        var ROIs = arrayOf<Mat>()
        for(i in 0 until contours.size)
        {
            var rect = Imgproc.boundingRect(contours[i])
            var ROI = Mat(picture, rect).clone()
            ROIs += ROI
        }
        return ROIs
    }

    fun displayPictures(picture : Mat, contours: Array<MatOfPoint>)
    {
        var pictureWithContours = picture.clone()
        Imgproc.drawContours(pictureWithContours, contours.toList(), -1, Scalar(0.0, 0.0, 0.0))

        HighGui.imshow("Original", picture)
        HighGui.imshow("Original avec contours", pictureWithContours)
        HighGui.waitKey(0)
    }

    fun getColorsFromPicture(takePicture : Boolean, fileName : String) : Array<Mat>
    {
        if(takePicture) takePictureAndSave(fileName)

        var picture = Imgcodecs.imread(fileName)

        Photo.fastNlMeansDenoisingColored(picture, picture)

        var contours = getContours()

        var ROIs = getROIs(picture, contours)

        if(showPictures) displayPictures(picture, contours)

        return ROIs
    }


}