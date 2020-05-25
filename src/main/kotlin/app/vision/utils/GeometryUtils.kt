package app.vision.utils

import org.koin.core.KoinComponent
import org.opencv.core.Point
import org.opencv.core.Scalar
import java.lang.Math.*

class GeometryUtils : KoinComponent {

    fun getDistance(a : Point, b : Point) : Double
    {
        return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))
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

    fun getDistance(a : Scalar, b : Scalar) : Double
    {
        return kotlin.math.sqrt(((a.`val`[0] - b.`val`[0]) * (a.`val`[0] - b.`val`[0])) + ((a.`val`[1] - b.`val`[1]) * (a.`val`[1] - b.`val`[1])) + ((a.`val`[2] - b.`val`[2]) * (a.`val`[2] - b.`val`[2])))
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

}