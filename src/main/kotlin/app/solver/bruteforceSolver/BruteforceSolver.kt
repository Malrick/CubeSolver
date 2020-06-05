package app.solver.bruteforceSolver

import app.model.cube.Cube
import app.model.movement.Movement
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.solver.Solver
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BruteforceSolver : Solver, KoinComponent {

    val movementService : MovementService by inject()

    abstract var listOfSequences : Set<Array<Movement>>

    val cubeMotionService : CubeMotionService by inject()

    override fun solve(cube : Cube) : Array<Movement>?
    {
        for(sequence in listOfSequences)
        {
            var clone = cube.clone()

            cubeMotionService.applySequence(clone, sequence)

            if(isSolved(clone))
            {
                return sequence
            }
        }
        return null
    }

    abstract fun isSolved(cube : Cube) : Boolean

}