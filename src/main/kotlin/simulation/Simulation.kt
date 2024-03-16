package simulation

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

data class SimulationResult(
    val state: State,
    val totalCollisions: Long,
)

object Simulation {
    private val queue = LinkedBlockingQueue<State>()
    private var time = 0.0
    private var totalCollisions = 0L
    private var lastState: State? = null

    fun start(initialState: State) {
        queue.clear()
        time = 0.0
        totalCollisions = 0L
        lastState = initialState

        var state = Optional.of(initialState)
        thread {
            while (true) {
                state = getNextCollisionState(state.get())
                if (state.isPresent) {
                    queue.add(state.get())
                } else {
                    break
                }
            }
        }
    }

    fun update(dt: Double): SimulationResult {
        if (lastState == null) {
            throw RuntimeException("Call start(initialState) first")
        }

        time += dt

        var lastCollisionState: State? = null
        while (true) {
            val firstCollisionTime = queue.firstOrNull()?.time ?: break
            if (firstCollisionTime > time) break

            lastCollisionState = queue.poll()
            totalCollisions++
        }

        val state = when (lastCollisionState) {
            null -> moveState(lastState!!)
            else -> moveState(lastCollisionState)
        }
        lastState = state
        return SimulationResult(
            state = state,
            totalCollisions = totalCollisions,
        )
    }

    private fun moveState(state: State): State {
        val stateDt = time - state.time
        return state.copy(
            time = time,
            box1 = state.box1.move(stateDt),
            box2 = state.box2.move(stateDt),
        )
    }
}