package pi.simulation

data class State(
    val time: Double,
    val box1: BoxState,
    val box2: BoxState,
)