package app.model.robot.vision

/*
    Different methods to be processing (classifying) colors with the robot
 */
enum class ColorProcessing {
    NeuralNetwork,
    Clustering,
    ClosestDistance
}