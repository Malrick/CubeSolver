package app

import app.UI.DisplayHelper
import app.helper.InitHelper
import app.helper.InputHelper
import app.robotLink.MotionService
import app.robotLink.ServoControler
import app.robotLink.ServoService
import app.service.CornerService
import app.service.CubeService
import app.service.EdgeService
import app.service.SideService
import app.solver.populationSolver.*
import org.koin.dsl.module

val helperModule = module{
    single { InitHelper() }
}

val displayModule = module{
    single { DisplayHelper() }
}

val serviceModule = module {
    single { CubeService() }
    single { SideService() }
    single { CornerService() }
    single { EdgeService() }
}

val solverModule = module {
    single { PopulationCrossSolver(10000, 0.001f, 3, true) }
    single { PopulationCornerSolver(10000, 0.001f, 4, true) }
    single { PopulationSecondFloorSolver(10000, 0.001f, 3, true) }
    single { PopulationOLLSolverOne(10000, 0.001f, 3, true) }
    single { PopulationOLLSolverTwo(10000, 0.001f, 3, true) }
    single { PopulationPLLSolverOne(10000, 0.001f, 3, true) }
    single { PopulationPLLSolverTwo(10000, 0.001f, 3, true) }
    single { PopulationCubeSolver(10000, 0.001f, 3, true) }
}

val servoModule = module {
    single{ ServoControler() }
    single{ ServoService() }
    single{ MotionService() }
}

val inputModule = module {
    single { InputHelper() }
}