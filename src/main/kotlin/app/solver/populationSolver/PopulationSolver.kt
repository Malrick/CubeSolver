package app.solver.populationSolver

import app.UI.ConsoleUI
import app.model.cube.Cube
import app.model.movement.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.solver.Solver
import app.utils.sequencer.BfsSequencer
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashMap

abstract class PopulationSolver : Solver, KoinComponent {

    val logger = LoggerFactory.getLogger(PopulationSolver::class.java)

    // Services / Helpers
    protected val cubeInformationService : CubeInformationService by inject()
    protected val cubeMotionService : CubeMotionService by inject()
    protected val movementService : MovementService by inject()
    private var random = Random()

    // Initializer
    protected val bfsSequencer : BfsSequencer by inject()

    // Studied set
    protected abstract var listOfMovements : Set<Array<Movement>>

    // Studied element
    protected lateinit var cubeExperimental : Cube

    // State of the algorithm
    protected abstract var maxScore : Int
    protected var solved = false
    private var population = HashMap<Array<Movement>, Int>()
    private var survivingPopulation = HashMap<Array<Movement>, Int>()

    // Parameters of the solver
    protected abstract val populationMaxSize : Int
    protected abstract val ratioOfSurvivingPopulation : Float
    protected abstract val maxNumberOfMutationsAdded : Int
    protected abstract val randomizeNumberOfMutation : Boolean

    override fun solve(cube : Cube) : Array<Movement>?
    {
        cubeExperimental = cube.clone()

        init(cube)

        while(!solved) {

            gradeIndividuals(cube)

            selectBestIndividuals()

            repopulate()

            logger.debug("Best individual reached " + population.values.max().toString() + " out of " + maxScore)

        }

        return movementService.convertToOptimalSequence(population.filter { it.value == maxScore }.keys.minBy { it.size }!!)

    }

    private fun init(cube : Cube) {

        // General initialization of the solver (maybe it is used to do more than one solving, need to re-init)
        solved = (gradeSequence(cube, arrayOf()) == maxScore)

        // If the cube is already solved, set the grade of empty solution to max and return
        if(solved)
        {
            population[arrayOf()] = maxScore
            return
        }

        survivingPopulation = HashMap()

        bfsSequencer.init(listOfMovements.toTypedArray())

        for(elem in bfsSequencer.getElements(populationMaxSize))
        {
            population.put(elem, -1)
        }

    }

    private fun gradeIndividuals(cube: Cube) {
        for (movements: Array<Movement> in population.keys) {
            if(population[movements] == -1)
            {
                population[movements] = gradeSequence(cube, movements)
            }
        }
    }

    private fun selectBestIndividuals() {
        val indexOfLimitIndividual = (populationMaxSize.toFloat() * (1.0 - ratioOfSurvivingPopulation)).toInt() - 1
        val gradeRequiredToSurvive = population.values.sorted()[indexOfLimitIndividual]
        survivingPopulation = population.filterValues { it >= gradeRequiredToSurvive } as HashMap<Array<Movement>, Int>

        if(survivingPopulation.size.toFloat() > (populationMaxSize * ratioOfSurvivingPopulation))
        {
            var comparator = compareByDescending<Pair<Array<Movement>, Int>>{it.second}.thenBy{ it.first.size }
            var sortedSurvivingPopulation = survivingPopulation.toList().sortedWith(comparator)
            survivingPopulation = HashMap(sortedSurvivingPopulation.subList(0, (populationMaxSize * ratioOfSurvivingPopulation).toInt()).toMap())
        }

        population = HashMap(survivingPopulation)
    }

    private fun repopulate() {
        var randomMutationSelection : Array<Movement>
        var listOfAddedMovements : Array<Movement>
        var parentPlusChild : Array<Movement> = arrayOf()
        var numberOfMutationsAdded = maxNumberOfMutationsAdded

        while (population.size < populationMaxSize) {
            val selectedParent = survivingPopulation.keys.elementAt(random.nextInt(survivingPopulation.size))

            if(randomizeNumberOfMutation)
            {
                numberOfMutationsAdded = random.nextInt(maxNumberOfMutationsAdded)
            }

            do
            {
                listOfAddedMovements = arrayOf()
                // Create a random mutation
                for(i in 0 until numberOfMutationsAdded) {

                    randomMutationSelection = listOfMovements.elementAt(random.nextInt(listOfMovements.size))
                    listOfAddedMovements += randomMutationSelection
                }
                parentPlusChild = selectedParent.plus(listOfAddedMovements)
            } while (!movementService.isOptimalSequence(parentPlusChild))

            // Add an individual with these mutations to the population
            population[parentPlusChild] = -1

            listOfAddedMovements = arrayOf()
        }
        survivingPopulation.clear()
    }

    protected abstract fun gradeSequence(cube : Cube, sequence : Array<Movement>) : Int
}