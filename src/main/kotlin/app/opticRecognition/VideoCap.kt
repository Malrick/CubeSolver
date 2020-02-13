package app.opticRecognition

import org.opencv.videoio.VideoCapture
import java.awt.image.BufferedImage

class VideoCap {

    lateinit var cam : VideoCapture
    var mat2Image = Mat2Image()

    fun openCam()
    {
        cam = VideoCapture()
        cam.open(0)
    }

    fun getOneFrame() : BufferedImage
    {
        cam.read(mat2Image.mat)
        return mat2Image.getImage(mat2Image.mat)!!
    }

}