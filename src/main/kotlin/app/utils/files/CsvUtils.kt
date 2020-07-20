package app.utils.files

import app.model.Color
import app.service.robot.ColorResolver
import app.utils.vision.ColorSpaceUtils
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.io.File
import kotlin.math.roundToInt

class CsvUtils : KoinComponent{

    private val robotColorService : ColorResolver by inject()
    private val colorSpaceUtils : ColorSpaceUtils by inject()

    /*
        Appends lab data useful for creating the neural network : white lab + color lab
     */
    fun appendLabDataInCsvFile(path: String, cubeColors : HashMap<Color, Array<Mat>>)
    {
        var file : File
        var folder = File("$path")
        var rows =  arrayOf<List<String>>()

        folder.mkdirs()

        var whiteLab = colorSpaceUtils.scalarBGR2Lab(robotColorService.getBgrDominantColor(cubeColors[Color.WHITE]!![4]))

        for(color in Color.values())
        {
            file = File("$path/${color.name}.csv")

            if(file.exists())
            {
                rows = csvReader().readAll(file).toTypedArray()
            }
            else
            {
                rows = Array(6) {listOf<String>()}
            }

            file.createNewFile()

            for(bgrDominantColor in robotColorService.getBgrDominantColors(cubeColors[color]!!))
            {
                var labDominantColor = colorSpaceUtils.scalarBGR2Lab(bgrDominantColor)
                rows[0] = rows[0].plus(whiteLab.`val`[0].roundToInt().toString())
                rows[1] = rows[1].plus(whiteLab.`val`[1].roundToInt().toString())
                rows[2] = rows[2].plus(whiteLab.`val`[2].roundToInt().toString())
                rows[3] = rows[3].plus(labDominantColor.`val`[0].roundToInt().toString())
                rows[4] = rows[4].plus(labDominantColor.`val`[1].roundToInt().toString())
                rows[5] = rows[5].plus(labDominantColor.`val`[2].roundToInt().toString())
            }

            csvWriter().open(file) { writeAll(rows.toList()) }
        }
    }

    /*
        Export Thistlethwaite values to csv
     */
    fun exportThistlethwaiteValuesToCsv(path : String, times : List<Long>, solutionLengths : List<Int>)
    {
        var solvingTimesFile : File
        var solutionLengthsFile : File
        var folder = File("$path")
        var rows =  arrayOf<List<String>>()

        folder.mkdirs()

        solvingTimesFile = File("$path/ThistlethwaiteSolvingTimes.csv")
        solutionLengthsFile = File("$path/ThistlethwaiteSolutionLengths.csv")

        if(solvingTimesFile.exists())
        {
            rows = csvReader().readAll(solvingTimesFile).toTypedArray()
        }
        else
        {
            rows = Array(5) {listOf<String>()}
        }

        solvingTimesFile.createNewFile()

        for(i in 0..4)
        {
            rows[i] = rows[i].plus(times[i].toString())
        }

        csvWriter().open(solvingTimesFile) { writeAll(rows.toList()) }

        if(solutionLengthsFile.exists())
        {
            rows = csvReader().readAll(solutionLengthsFile).toTypedArray()
        }
        else
        {
            rows = Array(5) {listOf<String>()}
        }


        solutionLengthsFile.createNewFile()

        for(i in 0..4)
        {
            rows[i] = rows[i].plus(solutionLengths[i].toString())
        }

        csvWriter().open(solutionLengthsFile) { writeAll(rows.toList()) }


    }

}