package app.solver.populationSolver

import app.model.Color
import app.model.cube.Cube
import app.model.movement.*
import app.service.orientation.OrientationService

class PopulationGroupSolver(
override val populationMaxSize: Int,
override val ratioOfSurvivingPopulation: Float,
override val maxNumberOfMutationsAdded: Int,
override val randomizeNumberOfMutation: Boolean
) : PopulationSolver() {

    override var maxScore = 400

    override var listOfMovements = Movement.values().map { arrayOf(it) }.toSet()

    override fun gradeSequence(cube: Cube, sequence: Array<Movement>): Int {

        var cubeExperiment = cube.clone()

        cubeMotionService.applySequence(cubeExperiment, sequence)

        var score = 400

        var groups = cubeInformationService.getGroups(cubeExperiment)

        var groupsBiggerThan3 = groups.filter { it.size>=3 && it.size <=5 }.size
        var groupsBiggerThan6 = groups.filter { it.size>=6 && it.size <11 }.size
        var groupsBiggerThan11 = groups.filter { it.size>=11 && it.size <15 }.size
        var groupsBiggerThan15 = groups.filter { it.size>=15 && it.size <18 }.size
        var groupsBiggerThan18 = groups.filter { it.size>=18 && it.size <21 }.size
        var groupsBiggerThan21 = groups.filter { it.size>=21 && it.size <24 }.size
        var groupsBiggerThan24 = groups.filter { it.size>=24 && it.size <26 }.size
        var oneGroup = groups.filter { it.size==26 }.size

        if(oneGroup == 1) solved = true

        else score = groupsBiggerThan3 * 10 + groupsBiggerThan6 * 30 + groupsBiggerThan11 * 70 + groupsBiggerThan15 * 120 + groupsBiggerThan18 * 150 + groupsBiggerThan21 * 200 + groupsBiggerThan24* 300

        return score
    }

}