package simulation

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

data class SimulationResult(
    val state: State,
    val totalCollisions: Long,
)

typealias SimulationResultListener = (SimulationResult) -> Unit

object Simulation {
    private val queue = LinkedBlockingQueue<State>()
    private var time = 0.0
    private var totalCollisions = 0L
    private var lastState: State? = null
    private var listener: SimulationResultListener? = null
    var isRunning = false
        private set

    fun start(initialState: State) {
        queue.clear()
        time = 0.0
        totalCollisions = 0L
        lastState = initialState
        isRunning = true

        var state = Optional.of(initialState)
        thread {
            while (isRunning) {
                state = getNextCollisionState(state.get())
                if (state.isPresent) {
                    queue.add(state.get())
                } else {
                    break
                }
            }
        }

        var lastTimeMillis = System.currentTimeMillis()
        thread {
            while (isRunning) {
                Thread.sleep(1000 / 48)  // 30 FPS

                val currentTimeMillis = System.currentTimeMillis()
                listener?.invoke(update((currentTimeMillis - lastTimeMillis) / 1000.0))
                lastTimeMillis = currentTimeMillis
            }
        }
    }

    fun stop() {
        isRunning = false
    }

    private fun update(dt: Double): SimulationResult {
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

    fun listen(listener: SimulationResultListener) {
        this.listener = listener
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