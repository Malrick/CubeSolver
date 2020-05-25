package app.solver.populationSolver

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.model.cube.Cube
import app.model.cubeUtils.Movement
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.solver.Solver
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.HashMap

abstract class PopulationSolver : Solver, KoinComponent {

    // TODO : reformater les solutions

    // Services / Helpers
    protected val initHelper  : InitHelper  by inject()
    protected val cubeInformationService : CubeInformationService by inject()
    protected val cubeMotionService : CubeMotionService by inject()
    private var random = Random()

    // Studied set
    protected abstract var listOfMovements : Set<Array<Movement>>

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

        init(cube)

        while(!solved) {

            gradeIndividuals(cube)

            selectBestIndividuals()

            repopulate()

            println(population.values.max()!!)

        }

        return population.filter { it.value == maxScore }.keys.minBy { it.size }!!

    }

    private fun init(cube : Cube) {

        // General initialization of the solver (maybe it is used to do more than one solving, needs to re-init)
        solved = (gradeSequence(cube, arrayOf()) == maxScore)
        if(solved)
        {
            population[arrayOf()] = maxScore
            return
        }
        population = HashMap()
        survivingPopulation = HashMap()

        // Initialization of the population : BFS
        var currentPosition = arrayOf(0)

        while (population.size < populationMaxSize) {

            //add an element at coordinates [currentDepth, currentBreath]
            var elementToAdd = arrayOf<Movement>()

            for (current: Int in currentPosition) {
                var toAdd = listOfMovements.elementAt(current)
                elementToAdd = elementToAdd.plus(toAdd)
            }

            population.put(elementToAdd, -1)

            // TODO simplifier
            currentPosition[currentPosition.size - 1]++
            if (currentPosition[currentPosition.size - 1] > listOfMovements.size - 1) {
                currentPosition[currentPosition.size - 1] = 0
                var machin = currentPosition.size - 1
                if (machin == 0) {
                    currentPosition += 0
                }
                if (machin > 0) {
                    currentPosition[machin - 1]++

                    while (machin >= 0) {
                        if (currentPosition[machin] > listOfMovements.size - 1) {
                            currentPosition[machin] = 0
                            if (machin > 0) {
                                currentPosition[machin - 1]++
                            } else {
                                currentPosition += 0
                            }
                        }
                        machin--
                    }
                }
            }
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

        // TODO Trouver un moyen de virer ce while
        while (survivingPopulation.size.toFloat() > (populationMaxSize * ratioOfSurvivingPopulation)) {

            // Getting the key of the first individual with minimum score and maximum key length
            val keyOfIndividualWithMinScoreAndMaxSize = survivingPopulation.filter { it.value == survivingPopulation.minBy { it.value }!!.value }.maxBy{ it.key.size}!!.key

            // Deleting the element with the minimum score and the maximum length
            survivingPopulation.remove(keyOfIndividualWithMinScoreAndMaxSize)
        }

        // Population is now survivingPopulation
        population = HashMap(survivingPopulation)
    }

    private fun repopulate() {
        // TODO : Déterminisme : Fouiller dans l'espace de solution sans random ?

        var randomMutationSelection : Array<Movement>
        var listOfAddedMovements : Array<Movement> = arrayOf()
        var numberOfMutationsAdded = maxNumberOfMutationsAdded

        while (population.size < populationMaxSize) {
            // Select a parent
            val selectedParent = survivingPopulation.keys.elementAt(random.nextInt(survivingPopulation.size))

            if(randomizeNumberOfMutation)
            {
                numberOfMutationsAdded = random.nextInt(maxNumberOfMutationsAdded)
            }

            // Create a random mutation
            for(i in 0..numberOfMutationsAdded) {

                do{
                    val suitableToAdd = true
                    randomMutationSelection = listOfMovements.elementAt(random.nextInt(listOfMovements.size))

                } while (!suitableToAdd)

                listOfAddedMovements += randomMutationSelection
            }

            // Add an individual with these mutations to the population
            population[selectedParent.plus(listOfAddedMovements)] = -1

            listOfAddedMovements = arrayOf()
        }
        survivingPopulation.clear()
    }

    protected abstract fun gradeSequence(cube : Cube, sequence : Array<Movement>) : Int
}