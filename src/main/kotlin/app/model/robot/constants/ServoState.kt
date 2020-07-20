package app.model.robot.constants

/*
    Different states that a servo can be in
 */
enum class ServoState {
    INSIDE,
    OUTSIDE,
    TURNED,
    NOT_TURNED,
    CURRENTLY_MOVING
}