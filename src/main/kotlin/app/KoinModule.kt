package app

import app.UI.ConsoleUI
import app.helper.InitHelper
import app.model.cube.piece.Edge
import app.service.cube.CubeInformationService
import app.service.cube.CubeMotionService
import app.service.robot.SequenceService
import app.service.robot.ServoService
import app.service.robot.MotionService
import app.service.cubeOLD.CornerService
import app.service.cubeOLD.CubeService
import app.service.cubeOLD.EdgeService
import app.service.cubeOLD.SideService
import app.solver.populationSolver.*
import app.vision.ColorResolver
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
    single { CubeService() }
    single { SideService() }
    single { EdgeService() }
    single { CornerService() }
}

val solverModule = module {
    single { PopulationCrossSolver(100, 0.05f, 3, true) }
    single { PopulationCornerSolver(100, 0.1f, 4, true) }
    single { PopulationSecondFloorSolver(10000, 0.001f, 3, true) }
    single { PopulationOLLSolverOne(10000, 0.001f, 3, true) }
    single { PopulationOLLSolverTwo(10000, 0.001f, 3, true) }
    single { PopulationPLLSolverOne(10000, 0.001f, 3, true) }
    single { PopulationPLLSolverTwo(10000, 0.001f, 3, true) }
}

val servoModule = module {
    single{ ServoService() }
    single{ MotionService() }
    single{ SequenceService() }
}

val visionModule = module {
    single{ ColorResolver()}
}