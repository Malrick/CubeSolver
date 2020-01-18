package app.helper

import app.model.Cube
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProjectionHelper : KoinComponent {

    val displayHelper : DisplayHelper by inject()

    fun makeProjections(cube : Cube)
    {
        var cornerNames = arrayOf<Char>()
        var cornerSolveds = arrayOf<Char>()
        var cornerBadOrientations = arrayOf<Char>()

        var edgeNames = arrayOf<Char>()
        var edgeSolveds = arrayOf<Char>()
        var edgeBadOrientations = arrayOf<Char>()

        for(c in cube.corners)
        {
            cornerNames += c.getName()

            if(c.isSolved()) { cornerSolveds += '1' }
            else { cornerSolveds += '0' }

            if(c.isBadlyOriented()){ cornerBadOrientations += '1' }
            else { cornerBadOrientations += '0' }
        }

        for(e in cube.edges)
        {
            edgeNames += e.getName()
            if(e.isSolved()) { edgeSolveds += '1' }
            else { edgeSolveds += '0' }

            if(e.isBadlyOriented()){ edgeBadOrientations += '1' }
            else { edgeBadOrientations += '0' }
        }

        println()
        println("Pièces : ")
        println()
        displayHelper.project(cornerNames, edgeNames)
        println()
        println("Résolues :")
        println()
        displayHelper.project(cornerSolveds, edgeSolveds)
        println()
        println("Mal orientées :")
        println()
        displayHelper.project(cornerBadOrientations, edgeBadOrientations)
    }

}