package app.utils.vision

import org.koin.core.KoinComponent
import org.opencv.core.Scalar
import kotlin.math.pow

class GeometryUtils : KoinComponent {

    fun getDistance(a : Scalar, b : Scalar) : Double
    {
        return Math.sqrt(
            (a.`val`[0] - b.`val`[0]).pow(2) + (a.`val`[1] - b.`val`[1]).pow(2) + (a.`val`[2] - b.`val`[2]).pow(
                2
            )
        )
    }

}