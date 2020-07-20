package app.utils.vision

import org.koin.core.KoinComponent
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/*
    Convert colors from a color space to another thanks to OpenCV
 */
class ColorSpaceUtils : KoinComponent {

    fun scalarsBGR2Lab(scalars : Array<Scalar>) : Array<Scalar>
    {
        var results = arrayOf<Scalar>()
        for(elem in scalars)
        {
            results += scalarBGR2Lab(elem)
        }
        return results
    }

    fun scalarBGR2Lab(scalar: Scalar) : Scalar
    {
        var bgr = Mat(1,1, CvType.CV_8UC3, Scalar(scalar.`val`[0], scalar.`val`[1], scalar.`val`[2]))
        var lab = Mat()
        Imgproc.cvtColor(bgr, lab, Imgproc.COLOR_BGR2Lab)
        return Scalar(lab[0,0][0]/255.0*100.0, lab[0,0][1]-128.0, lab[0,0][2]-128.0)
    }

    fun BgrToHsv(colors : Array<Scalar>) : Array<Scalar>
    {
        var HsvColors = arrayOf<Scalar>()
        for(color in colors)
        {
            HsvColors += scalarBgr2Hsv(color.`val`[0], color.`val`[1], color.`val`[2])
        }
        return HsvColors
    }

    fun scalarBgr2Hsv(B : Double, G : Double, R : Double) : Scalar
    {
        var bgr = Mat(1,1, CvType.CV_8UC3, Scalar(B,G,R))
        var hsv = Mat()
        Imgproc.cvtColor(bgr, hsv, Imgproc.COLOR_BGR2HSV)
        // TODO : normalize
        return Scalar(hsv[0,0][0]/255.0*100.0, hsv[0,0][1]-128.0, hsv[0,0][2]-128.0)
    }

}