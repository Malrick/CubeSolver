package app

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.robot.RobotSequenceService
import app.service.robot.ServoService
import app.service.robot.RobotMotionService
import app.vision.ColorResolver
import app.vision.VideoManager
import app.vision.utils.ColorUtils
import org.koin.dsl.module

val helperModule = module{
    single { InitHelper() }
}

val displayModule = module{
    single { ConsoleUI() }
}

val serviceModule = module {
    single { CubeInformationService() }
    single { CubeMotionService() }
}

val servoModule = module {
    single{ ServoService() }
    single{ RobotMotionService() }
    single{ RobotSequenceService() }
}

val visionModule = module {
    single{ VideoManager() }
    single{ ColorUtils()}
    single{ ColorResolver()}
}