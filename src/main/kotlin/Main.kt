import simulation.BoxState
import simulation.State
import simulation.getNextCollisionStates
import java.util.*

fun main() {
    var state = Optional.of(
        State(
            time = 0.0,
            box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
            box2 = BoxState(position = 10.0, velocity = -1.0, mass = 1, width = 1, height = 1),
        )
    )

    var collisions = 0
    while (state.isPresent) {
        println(state)

        state = getNextCollisionStates(state.get())
        if (state.isPresent) {
            collisions++
        }
    }
    println("Total $collisions collisions")
}