package pi.rendering

import pi.simulation.Simulation
import javax.swing.JButton

private val SPEEDS = listOf(
    0.5 to "x 0.5",
    1.0 to "x 1",
    2.0 to "x 2",
)

class SpeedButton : JButton("x 1") {
    private var speedIndex = 1

    init {
        addActionListener {
            speedIndex = (speedIndex + 1) % SPEEDS.size
            Simulation.playSpeed = SPEEDS[speedIndex].first
            text = SPEEDS[speedIndex].second
        }
    }
}