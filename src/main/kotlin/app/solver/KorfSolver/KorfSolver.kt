package app.solver.KorfSolver

import app.model.Color
import app.model.cube.Cube
import app.model.cube.position.CornerPosition
import app.model.cube.position.EdgePosition
import app.model.database.Database
import app.model.movement.Movement
import app.model.movement.RelativePosition
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.orientation.OrientationService
import app.solver.Solver
import app.utils.algorithms.graphTraversal.BFS
import app.utils.database.DatabaseUtils
import app.utils.indexing.LehmerRanker
import app.utils.maths.MathUtils
import org.koin.core.KoinComponent
import org.koin.core.inject

class KorfSolver : Solver, KoinComponent {

    val lehmerRanker : LehmerRanker by inject()
    val cubeInformationService : CubeInformationService by inject()
    val cubeMotionService : CubeMotionService by inject()
    val cubeInitializationService : CubeInitializationService by inject()
    val orientationService : OrientationService by inject()
    val mathUtils : MathUtils by inject()

    val databaseUtils : DatabaseUtils by inject()

    override fun solve(cube: Cube): Array<Movement>? {
        TODO("Not yet implemented")
    }

    fun testDatabase()
    {
        val myOrientation = orientationService.getOrientation(Pair(RelativePosition.TOP, Color.WHITE), Pair(
            RelativePosition.FRONT, Color.GREEN))


        var cube = Cube(3, myOrientation)

        cubeInitializationService.initSolvedCube(cube)

        for(i in 0..50)
        {
            var randomMovement = Movement.values().random()

            cubeMotionService.applyMovement(cube, randomMovement)

            print(randomMovement.toString() + " ")

            var cornerOrientationArray = arrayOf<Int>()
            var cornerPosition = arrayOf<Int>()
            var edgeOrientationArray = arrayOf<Int>()
            var edgePosition = arrayOf<Int>()

            var cornerOrientations = cubeInformationService.getCornerOrientations(cube)
            var sortedCornerPositions = cube.positions.keys.filter { it is CornerPosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending{ it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height }
            var edgeOrientations = cubeInformationService.getEdgeOrientations(cube)
            var sortedEdgePositions = cube.positions.keys.filter { it is EdgePosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending{ it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height }

            for(corner in sortedCornerPositions) {
                cornerOrientationArray += cornerOrientations[corner]!!
                cornerPosition += sortedCornerPositions.indexOfFirst { it.getColors() == cube.positions[corner]!!.getColors() }
            }


            for(edge in sortedEdgePositions) {
                edgeOrientationArray += edgeOrientations[edge]!!
                edgePosition += sortedEdgePositions.indexOfFirst { it.getColors() == cube.positions[edge]!!.getColors() }
            }

            var cornerLehmerCode = lehmerRanker.lookupCornersPosition(cornerPosition) * Math.pow(3.0,7.0).toInt() + lehmerRanker.lookupCornersOrientation(cornerOrientationArray)
            var edgeLehmerCode = lehmerRanker.lookupEdgesPosition(edgePosition.slice(IntRange(0, 5)).toTypedArray()) * mathUtils.pfactorial(12, 5) * lehmerRanker.lookupEdgesOrientation(edgeOrientationArray.slice(IntRange(0, 5)).toTypedArray())
            var edgeLehmerCode2 = lehmerRanker.lookupEdgesPosition(edgePosition.slice(IntRange(6, 11)).toTypedArray()) * mathUtils.pfactorial(12, 5) * lehmerRanker.lookupEdgesOrientation(edgeOrientationArray.slice(IntRange(6, 11)).toTypedArray())

            var databaseElem = databaseUtils.queryElement(Database.CORNERS, cornerLehmerCode.toString())


            var databaseElem2 = databaseUtils.queryElement(Database.EDGES, edgeLehmerCode.toString())

            var databaseElem3 = databaseUtils.queryElement(Database.EDGES2, edgeLehmerCode2.toString())

            print(databaseElem + " ")

            print(databaseElem2 + " ")
            println(databaseElem3 + " ")
            println()

        }
    }

    fun populateDatabase()
    {
        val myOrientation = orientationService.getOrientation(Pair(RelativePosition.TOP, Color.WHITE), Pair(
            RelativePosition.FRONT, Color.GREEN))

        var bfs = BFS()

        bfs.init( Movement.values().map { arrayOf(it) }.toTypedArray())

        var move = bfs.getElement()

        var cube = Cube(3, myOrientation)

        val cubeInitializationService = CubeInitializationService()

        while(move.size < 12) {

            cubeInitializationService.initSolvedCube(cube)

            cubeMotionService.applySequence(cube, move)

            var cornerOrientationArray = arrayOf<Int>()
            var cornerPosition = arrayOf<Int>()
            var edgeOrientationArray = arrayOf<Int>()
            var edgePosition = arrayOf<Int>()

            var sortedCornerPositions = cube.positions.keys.filter { it is CornerPosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending{ it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height }
            var sortedEdgePositions = cube.positions.keys.filter { it is EdgePosition }.sortedBy { it.cubeCoordinates.width }.sortedByDescending{ it.cubeCoordinates.depht }.sortedByDescending { it.cubeCoordinates.height }

            for(corner in sortedCornerPositions) {
                cornerOrientationArray += cubeInformationService.getCornerOrientations(cube)[corner]!!
                cornerPosition += sortedCornerPositions.indexOfFirst { it.getColors() == cube.positions[corner]!!.getColors() }
            }


            for(edge in sortedEdgePositions) {
                edgeOrientationArray += cubeInformationService.getEdgeOrientations(cube)[edge]!!
                edgePosition += sortedEdgePositions.indexOfFirst { it.getColors() == cube.positions[edge]!!.getColors() }
            }

            var cornerLehmerCode = lehmerRanker.lookupCornersPosition(cornerPosition) * Math.pow(3.0,7.0).toInt() + lehmerRanker.lookupCornersOrientation(cornerOrientationArray)
            var edgeLehmerCode = lehmerRanker.lookupEdgesPosition(edgePosition.slice(IntRange(0, 5)).toTypedArray()) * mathUtils.pfactorial(12, 5) * lehmerRanker.lookupEdgesOrientation(edgeOrientationArray.slice(IntRange(0, 5)).toTypedArray())
            var edgeLehmerCode2 = lehmerRanker.lookupEdgesPosition(edgePosition.slice(IntRange(6, 11)).toTypedArray()) * mathUtils.pfactorial(12, 5) * lehmerRanker.lookupEdgesOrientation(edgeOrientationArray.slice(IntRange(6, 11)).toTypedArray())


            var databaseElem = databaseUtils.queryElement(Database.CORNERS, cornerLehmerCode.toString())

            if(databaseElem != null)
            {
                if(move.size < databaseElem.toInt()!!)
                    databaseUtils.addElement(Database.CORNERS, cornerLehmerCode.toString(), move.size.toString())
            }
            else
            {
                databaseUtils.addElement(Database.CORNERS, cornerLehmerCode.toString(), move.size.toString())
            }

            databaseElem = databaseUtils.queryElement(Database.EDGES, edgeLehmerCode.toString())

            if(databaseElem != null)
            {
                if(move.size < databaseElem.toInt())
                    databaseUtils.addElement(Database.EDGES, edgeLehmerCode.toString(), move.size.toString())
            }
            else
            {
                databaseUtils.addElement(Database.EDGES, edgeLehmerCode.toString(), move.size.toString())
            }

            databaseElem = databaseUtils.queryElement(Database.EDGES2, edgeLehmerCode2.toString())

            if(databaseElem != null)
            {
                if(move.size < databaseElem.toInt())
                    databaseUtils.addElement(Database.EDGES2, edgeLehmerCode2.toString(), move.size.toString())
            }
            else
            {
                databaseUtils.addElement(Database.EDGES2, edgeLehmerCode2.toString(), move.size.toString())
            }


            move = bfs.getElement()
        }
    }
}