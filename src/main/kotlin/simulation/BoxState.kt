package simulation

data class BoxState(
    val position: Double,
    val velocity: Double,
    val mass: Long,
    val width: Int,
    val height: Int,
) {
    fun move(dt: Double): BoxState {
        return copy(position = position + velocity * dt)
    }
}
