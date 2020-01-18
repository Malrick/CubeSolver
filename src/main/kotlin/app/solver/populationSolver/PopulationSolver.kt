package app.solver.populationSolver

import app.helper.InitHelper
import app.helper.InputHelper
import app.model.Cube
import app.model.constants.Movement
import app.service.CubeService
import app.service.SideService
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.HashMap

open abstract class PopulationSolver : KoinComponent {

    // TODO : reformater les solutions

    // Services / Helpers
    protected val inputHelper : InputHelper by inject()
    protected val initHelper  : InitHelper  by inject()
    protected val cubeService : CubeService by inject()
    private var random = Random()

    // Studied set
    protected abstract var listOfMovements : Set<Array<Movement>>

    // State of the algorithm
    protected var solved = false
    private var population = HashMap<Array<Movement>, Int>()
    private var survivingPopulation = HashMap<Array<Movement>, Int>()

    // Parameters of the solver
    private val populationMaxSize = 20000
    private val ratioOfSurvivingPopulation = 0.001
    private val maxNumberOfMutationsAdded = 5
    private val randomizeNumberOfMutation = true

    fun getSolution(cube : Cube) : Array<Movement>
    {

        init()

        while(!solved) {

            gradeIndividuals(cube)

            selectBestIndividuals()

            repopulate()

        }

        return population.filter { it.value == 400 }.keys.sortedBy { it.size }.first()

    }

    private fun init() {

        // General initialization of the solver (maybe it is used to solve many times)
        solved = false
        population = HashMap()
        survivingPopulation = HashMap()

        // Initialization of the population : BFS
        var currentPosition = arrayOf(0)

        while (population.size < populationMaxSize) {

            //add an element at coordinates [currentDepth, currentBreath]
            var elementToAdd = arrayOf<Movement>()

            for (current: Int in currentPosition) {
                elementToAdd += listOfMovements.elementAt(current)
            }

            population.put(elementToAdd, 0)

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
            population[movements] = gradeSequence(cube, movements)
        }
    }

    private fun selectBestIndividuals() {
        var indexOfLimitIndividual = (populationMaxSize.toFloat() * (1.0 - ratioOfSurvivingPopulation)).toInt() - 1
        var gradeRequiredToSurvive = population.values.sorted()[indexOfLimitIndividual]
        survivingPopulation = population.filterValues { it >= gradeRequiredToSurvive } as HashMap<Array<Movement>, Int>

        // Value of the minimum score of the surviving population
        var minScore : Int

        // Individuals which are having the minimum score
        var individualsWithMinScore : HashMap<Array<Movement>, Int>

        // Value of the maximum length of the list of movements of the individuals which are having the minimum score
        var maxLengthOfIndividualsWithMinScore = 0

        // Key of the individual which is having the minimum score, and with the maximum length of movements (individuals to delete)
        var keyOfIndividualWithMinScoreAndMaxSize : Array<Movement> = arrayOf()

        while (survivingPopulation.size.toFloat() > (populationMaxSize * ratioOfSurvivingPopulation)) {
            // todo Faire une map clé -> Taille de la clé des scores min ?

            // Getting the minimum score of the surviving population
            minScore = survivingPopulation.values.sorted()[0]

            // Getting all individuals which are having the minimum score
            individualsWithMinScore = survivingPopulation.filter { it.value == minScore } as HashMap<Array<Movement>, Int>

            // Among these individuals with the minimum score, getting the maximum length of the key
            for(elem in individualsWithMinScore)
            {
                if(elem.key.size >= maxLengthOfIndividualsWithMinScore)
                {
                    maxLengthOfIndividualsWithMinScore = elem.key.size
                    keyOfIndividualWithMinScoreAndMaxSize = elem.key
                }
            }

            // Deleting the element with the minimum score and the maximum length
            survivingPopulation.remove(keyOfIndividualWithMinScoreAndMaxSize)

            maxLengthOfIndividualsWithMinScore = 0
            individualsWithMinScore.clear()
        }

        population.clear()

        population = HashMap(survivingPopulation)
    }

    private fun repopulate() {
        // TODO : Déterminisme : Fouiller dans l'espace de solution sans random ?

        var randomMutationSelection : Array<Movement>
        var listOfAddedMovements : Array<Movement> = arrayOf()
        var numberOfMutationsAdded = maxNumberOfMutationsAdded

        while (population.size < populationMaxSize) {
            // Select a parent
            var selectedParent = survivingPopulation.keys.elementAt(random.nextInt(survivingPopulation.size))

            if(randomizeNumberOfMutation)
            {
                numberOfMutationsAdded = random.nextInt(maxNumberOfMutationsAdded)
            }

            // Create a random mutation
            for(i in 0.. numberOfMutationsAdded) {

                // TODO c'est moche
                do{
                    var suitableToAdd = true
                    randomMutationSelection = listOfMovements.elementAt(random.nextInt(listOfMovements.size))

                } while (suitableToAdd == false)



                listOfAddedMovements += randomMutationSelection
            }

            // Add an individual with these mutations to the population
            population[selectedParent.plus(listOfAddedMovements)] = 0

            listOfAddedMovements = arrayOf()
        }
        survivingPopulation.clear()
    }

    protected abstract fun gradeSequence(cube : Cube, sequence : Array<Movement>) : Int
}