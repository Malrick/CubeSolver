package app

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.robot.RobotSequenceService
import app.service.robot.ServoService
import app.service.robot.RobotMotionService
import app.solver.bruteforceSolver.BruteforceOLLSolver
import app.solver.bruteforceSolver.BruteforcePLLSolver
import app.solver.populationSolver.*
import app.vision.ColorResolver
import app.vision.utils.ColorUtils
import app.vision.utils.GeometryUtils
import app.vision.utils.PictureProcessingUtils
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
    single{ ColorResolver()}
    single{ ColorUtils()}
    single{ GeometryUtils()}
    single{ PictureProcessingUtils()}
}