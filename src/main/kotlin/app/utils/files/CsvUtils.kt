package app.utils.files

import app.model.Color
import app.service.robot.RobotColorService
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.io.File

class CsvUtils : KoinComponent{

    val robotColorService : RobotColorService by inject()

    fun appendLabDataInCsvFile(path: String, cubeColors : HashMap<Color, Array<Mat>>)
    {
        var file : File
        var folder = File("$path")
        var rows =  arrayOf<List<String>>()

        folder.mkdirs()

        var whiteLab = robotColorService.getBgrDominantColor(cubeColors[Color.WHITE]!![4])

        for(color in Color.values())
        {
            file = File("$path/${color.name}.csv")

            if(file.exists())
            {
                rows = csvReader().readAll(file).toTypedArray()
            }

            file.createNewFile()

            for(dominantColor in robotColorService.getDominantColors(cubeColors[color]!!))
            {
                rows[0] += rows[0].plus(whiteLab.`val`[0].toString())
                rows[1] += rows[1].plus(whiteLab.`val`[1].toString())
                rows[2] += rows[2].plus(whiteLab.`val`[2].toString())
                rows[3] += rows[3].plus(dominantColor.`val`[0].toString())
                rows[4] += rows[4].plus(dominantColor.`val`[1].toString())
                rows[5] += rows[5].plus(dominantColor.`val`[2].toString())
            }

            csvWriter().open(file) { writeAll(rows.toList()) }
        }
    }

}