package app.utils.files

import app.model.Color
import app.service.robot.RobotColorService
import app.utils.vision.ColorSpaceUtils
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.opencv.core.Mat
import java.io.File
import kotlin.math.roundToInt

class CsvUtils : KoinComponent{

    private val robotColorService : RobotColorService by inject()
    private val colorSpaceUtils : ColorSpaceUtils by inject()


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

            for(bgrDominantColor in robotColorService.getDominantColors(cubeColors[color]!!))
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

}