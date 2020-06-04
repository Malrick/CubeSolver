package app.vision

import org.opencv.core.Core
import org.opencv.core.Mat
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte


class Mat2Image {
    companion object {
        init {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        }
    }

    var mat = Mat()
    var img: BufferedImage? = null

    constructor()
    constructor(mat: Mat) {
        getSpace(mat)
    }

    fun getSpace(mat: Mat) {
        var type = 0
        type = BufferedImage.TYPE_3BYTE_BGR
        this.mat = mat
        val w = mat.cols()
        val h = mat.rows()
        if (img == null || img!!.width != w || img!!.height != h || img!!.type != type) img =
            BufferedImage(w, h, type)
    }

    fun getImage(mat: Mat): BufferedImage? {
        getSpace(mat)
        val raster = img!!.raster
        val dataBuffer = raster.dataBuffer as DataBufferByte
        val data = dataBuffer.data
        mat[0, 0, data]
        return img
    }


}