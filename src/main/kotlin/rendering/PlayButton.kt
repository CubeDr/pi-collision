package rendering

import simulation.BoxState
import simulation.Simulation
import simulation.State
import javax.swing.JButton

class PlayButton : JButton("Play") {
    init {
        addActionListener {
            if (Simulation.isRunning) {
                Simulation.stop()
            } else {
                Simulation.start(
                    State(
                        time = 0.0,
                        box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
                        box2 = BoxState(
                            position = 10.0,
                            velocity = -2.0,
                            mass = 1,
                            width = 1,
                            height = 1
                        ),
                    )
                )
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