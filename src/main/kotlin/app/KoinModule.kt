package app

import app.helper.DisplayHelper
import app.helper.InitHelper
import app.helper.InputHelper
import app.helper.ProjectionHelper
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
    single { ProjectionHelper() }
    single { DisplayHelper() }
}

val serviceModule = module {
    single { CubeService() }
    single { SideService() }
    single { CornerService() }
    single { EdgeService() }
}

val solverModule = module {
    single { PopulationCrossSolver() }
    single { PopulationCornerSolver() }
    single { PopulationSecondFloorSolver() }
    single { PopulationOLLSolverOne() }
    single { PopulationOLLSolverTwo() }
    single { PopulationPLLSolverOne() }
    single { PopulationPLLSolverTwo() }
}

val inputModule = module {
    single { InputHelper() }
}