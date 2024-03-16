package simulation

import java.util.*
import kotlin.math.abs

fun getNextCollisionState(state: State): Optional<State> {
    val wallCollisionDt = getWallCollisionDt(state.box1)
    val boxCollisionDt = getBoxCollisionDt(state.box1, state.box2)

    if (wallCollisionDt < boxCollisionDt) {
        return Optional.of(getWallCollisionState(state, wallCollisionDt))
    } else if (boxCollisionDt < wallCollisionDt) {
        return Optional.of(getBoxCollisionState(state, boxCollisionDt))
    }

    return Optional.empty()
}

private fun getWallCollisionDt(box1: BoxState): Double {
    if (box1.velocity >= 0) return Double.MAX_VALUE

    val distance = box1.position - box1.width / 2.0
    val velocity = abs(box1.velocity)
    return distance / velocity
}

private fun getBoxCollisionDt(box1: BoxState, box2: BoxState): Double {
    if (box1.velocity < box2.velocity) return Double.MAX_VALUE

    val distance = (box2.position - box2.width / 2.0) - (box1.position + box1.width / 2.0)
    val velocity = abs(box2.velocity - box1.velocity)
    return distance / velocity
}

private fun getWallCollisionState(state: State, dt: Double) = state.copy(
    time = state.time + dt,
    box1 = state.box1.copy(position = state.box1.width / 2.0, velocity = -state.box1.velocity),
    box2 = state.box2.move(dt)
)

private fun getBoxCollisionState(state: State, dt: Double): State {
    val m1 = state.box1.mass
    val m2 = state.box2.mass
    val v1 = state.box1.velocity
    val v2 = state.box2.velocity

    // https://github.com/CubeDr/pi-collision/assets/13654700/997994a3-f87e-4f2d-ae69-3b6fac7079bb
    val x = (m1 * v1 - m2 * v1 + 2 * m2 * v2) / (m1 + m2)
    val y = (2 * m1 * v1 - m1 * v2 + m2 * v2) / (m1 + m2)

    return state.copy(
        time = state.time + dt,
        box1 = state.box1.copy(
            position = state.box1.position + state.box1.velocity * dt,
            velocity = x
        ),
        box2 = state.box2.copy(
            position = state.box2.position + state.box2.velocity * dt,
            velocity = y
        )
    )
}