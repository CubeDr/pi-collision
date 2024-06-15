package pi.rendering

import pi.simulation.MassControl
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JPanel
import kotlin.system.exitProcess

class PiCollisionFrame : Frame("Pi Collision") {
    init {
        setSize(400, 300)

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

        val controlPanel = JPanel(BorderLayout(80, 0)).apply {
            val buttonPanel = JPanel(FlowLayout()).apply {
                add(PlayButton())
                add(SpeedButton())
            }
            add(buttonPanel, BorderLayout.EAST)
            add(MassControl(), BorderLayout.CENTER)
        }
        add(controlPanel, BorderLayout.SOUTH)
    }
}