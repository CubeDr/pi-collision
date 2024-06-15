package pi.simulation

data class BoxState(
    val position: Double,
    val velocity: Double,
    val mass: Long,
    val width: Double,
    val height: Double,
) {
    fun move(dt: Double): BoxState {
        return copy(position = position + velocity * dt)
    }
}
