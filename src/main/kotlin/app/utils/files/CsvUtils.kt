package app.utils.files

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.opencv.core.Scalar
import java.io.File

class CsvUtils {

    fun appendLabDataInCsvFile(filePath : String, fileName : String, whiteData : List<Scalar>, labData : List<Scalar>)
    {
        var firstRow = listOf<Any>()
        var secondRow = listOf<Any>()
        var thirdRow = listOf<Any>()
        var fourthRow = listOf<Any>()
        var fifthRow = listOf<Any>()
        var sixthRow = listOf<Any>()

        firstRow = listOf()
        secondRow = listOf()
        thirdRow = listOf()
        fourthRow = listOf()
        fifthRow = listOf()
        sixthRow = listOf()

        var folder = File("$filePath")
        var file = File("$filePath$fileName.csv")

        folder.mkdirs()

        if(file.exists())
        {
            val oldRows: List<List<String>> = csvReader().readAll(file)

            firstRow+=oldRows[0]
            secondRow+=oldRows[1]
            thirdRow+=oldRows[2]
            fourthRow +=oldRows[3]
            fifthRow +=oldRows[4]
            sixthRow +=oldRows[5]
        }
        file.createNewFile()

        for(elem in labData)
        {
            firstRow+=whiteData[0].`val`[0].toInt()
            secondRow+=whiteData[0].`val`[1].toInt()
            thirdRow+=whiteData[0].`val`[2].toInt()
            fourthRow+=elem.`val`[0].toInt()
            fifthRow+=elem.`val`[1].toInt()
            sixthRow+=elem.`val`[2].toInt()
        }


        var rows = listOf(firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow)
        csvWriter().open(file) { writeAll(rows) }
    }

}