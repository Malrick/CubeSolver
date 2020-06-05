package app

import app.UI.ConsoleUI
import app.UI.MatricesDisplay
import app.service.cube.CubeInformationService
import app.service.cube.CubeInitializationService
import app.service.cube.CubeMotionService
import app.service.movement.MovementService
import app.service.orientation.OrientationService
import app.service.robot.RobotSequenceService
import app.service.robot.ServoService
import app.service.robot.RobotMotionService
import app.service.robot.ColorService
import app.service.robot.RobotVisionService
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
    single { RobotSequenceService() }
    single { RobotVisionService() }

    single { MovementService() }

    single { OrientationService() }
}

val visionModule = module {
    single { ColorSpaceUtils() }
    single { GeometryUtils() }
    single { ColorService() }
}