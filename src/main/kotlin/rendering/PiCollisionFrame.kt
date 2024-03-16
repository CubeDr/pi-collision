package rendering

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess

class PiCollisionFrame : Frame("Pi Collision") {
    init {
        setSize(500, 400)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                exitProcess(0)
            }
        })

        initLayout()
    }

    private fun initLayout() {
        layout = BorderLayout()

        val canvas = PiCollisionCanvas()
        add(canvas, BorderLayout.CENTER)

        val controlPanel = JPanel(BorderLayout()).apply {
            val buttonPanel = JPanel(FlowLayout()).apply {
                add(PlayButton())

                val speedButton = JButton("x 1")
                add(speedButton)
            }
            add(buttonPanel, BorderLayout.EAST)

            val massPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                val label = JLabel("Mass: 100")
                add(label)

                val slider = JSlider(JSlider.HORIZONTAL, 1, 12, 1)
                add(slider)
            }
            add(massPanel, BorderLayout.WEST)
        }
        add(controlPanel, BorderLayout.SOUTH)
    }
}