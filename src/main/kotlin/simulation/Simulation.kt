package simulation

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

data class SimulationResult(
    val state: State,
    val totalCollisions: Long,
)

typealias SimulationResultListener = (SimulationResult) -> Unit
typealias SimulationRunningStateListener = (isRunning: Boolean) -> Unit

object Simulation {
    private val queue = LinkedBlockingQueue<State>()
    private var time = 0.0
    private var totalCollisions = 0L
    private var listener: SimulationResultListener? = null
    private var runningStateListeners = mutableListOf<SimulationRunningStateListener>()
    var state: State? = null
        set(value) {
            field = value
            if (value != null) {
                listener?.invoke(SimulationResult(state!!, 0))
            }
        }

    var isRunning = false
        private set(value) {
            field = value
            runningStateListeners.forEach {
                it(value)
            }
        }
    var playSpeed = 1.0

    fun start() {
        queue.clear()
        time = 0.0
        totalCollisions = 0L
        isRunning = true

        if (state == null) {
            throw Exception("State not set.")
        }
        var state = Optional.of(state!!)
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
                listener?.invoke(update((currentTimeMillis - lastTimeMillis) * playSpeed / 1000.0))
                lastTimeMillis = currentTimeMillis
            }
        }
    }

    fun stop() {
        isRunning = false
    }

    private fun update(dt: Double): SimulationResult {
        if (state == null) {
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
            null -> moveState(state!!)
            else -> moveState(lastCollisionState)
        }
        this.state = state
        return SimulationResult(
            state = state,
            totalCollisions = totalCollisions,
        )
    }

    fun onStateUpdate(listener: SimulationResultListener) {
        this.listener = listener
        if (state != null) {
            listener.invoke(SimulationResult(state!!, 0))
        }
    }

    fun onRunningStateUpdate(listener: SimulationRunningStateListener) {
        runningStateListeners.add(listener)
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