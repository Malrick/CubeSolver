package app

import app.UI.ConsoleUI
import app.UI.MatricesDisplay
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.service.orientation.OrientationService
import app.service.robot.RobotOtvintaService
import app.service.robot.ServoService
import app.service.robot.RobotMotionService
import app.service.robot.RobotColorService
import app.service.robot.RobotVisionService
import app.utils.algorithms.BFS
import app.utils.algorithms.KNN
import app.utils.database.DatabaseUtils
import app.utils.files.CsvUtils
import app.utils.indexing.LehmerRanker
import app.utils.maths.MathUtils
import app.utils.types.TypeUtils
import app.utils.vision.ColorSpaceUtils
import app.utils.vision.GeometryUtils
import org.koin.dsl.module

val displayModule = module{
    single { ConsoleUI() }
    single { MatricesDisplay() }
}

val serviceModule = module {
    single { CubeInformationService() }
    single { CubeInitializationService() }
    single { CubeMotionService() }

    single { ServoService() }
    single { RobotMotionService() }
    single { RobotOtvintaService() }
    single { RobotVisionService() }

    single { MovementService() }

    single { OrientationService() }
}

val visionModule = module {
    single { ColorSpaceUtils() }
    single { GeometryUtils() }
    single { RobotColorService() }
}

val utilsModule = module {
    single { LehmerRanker() }
    single { DatabaseUtils() }
    single { MathUtils() }
    single { TypeUtils() }
    single { CsvUtils() }
    single { KNN() }
    single { BFS() }
}