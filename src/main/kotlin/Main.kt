import rendering.PiCollisionFrame
import simulation.BoxState
import simulation.Simulation
import simulation.State

fun main() {
    Simulation.state =
        State(
            time = 0.0,
            box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
            box2 = BoxState(
                position = 10.0,
                velocity = -2.0,
                mass = 1,
                width = 1,
                height = 1
            ),
        )

    val frame = PiCollisionFrame()
    frame.isVisible = true
}