package rendering

import simulation.BoxState
import simulation.Simulation
import simulation.State
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import kotlin.math.log
import kotlin.math.pow

class MassControl : JPanel() {
    private val label = JLabel()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        add(label)

        onMassChanged(100)
        val slider = JSlider(JSlider.HORIZONTAL, 0, 12, 2).apply {
            addChangeListener {
                onMassChanged(10.0.pow(value.toDouble()).toLong())
            }
        }
        add(slider)
    }

    private fun onMassChanged(mass: Long) {
        label.text = "Mass: $mass"

        val size = log(mass.toDouble(), 100.0).toInt() + 1
        Simulation.initialState =
            State(
                time = 0.0,
                box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
                box2 = BoxState(
                    position = 10.0,
                    velocity = -2.0,
                    mass = mass,
                    width = size,
                    height = size
                ),
            )
    }
}