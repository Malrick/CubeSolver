package app.solver

import app.model.cube.Cube
import app.model.cubeUtils.Movement

interface Solver {
    fun solve(cube : Cube) : Array<Movement>?
}