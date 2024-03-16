import simulation.BoxState
import simulation.Simulation
import simulation.State

fun main() {
    Simulation.start(State(
        time = 0.0,
        box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
        box2 = BoxState(position = 10.0, velocity = -1.0, mass = 1, width = 1, height = 1),
    ))

    for (time in 0..20) {
        val result = Simulation.update(1)
        println(result)
    }
}