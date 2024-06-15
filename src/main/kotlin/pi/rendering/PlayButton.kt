package pi.rendering

import pi.simulation.Simulation
import javax.swing.JButton

class PlayButton : JButton("Play") {
    init {
        addActionListener {
            if (Simulation.isRunning) {
                Simulation.stop()
            } else {
                Simulation.start()
            }
        }

        Simulation.onRunningStateUpdate { isRunning ->
            text = when (isRunning) {
                true -> "Stop"
                false -> "Play"
            }
        }
    }
}