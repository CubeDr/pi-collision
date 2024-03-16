package rendering

import simulation.BoxState
import simulation.Simulation
import simulation.SimulationResult
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

class PiCollisionCanvas : Canvas() {
    private val scale = 30
    private var simulationResult: SimulationResult? = null
    private var buffer: Image? = null

    init {
        Simulation.listen {
            simulationResult = it
            repaint()
        }

        addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                val size = e?.component?.size ?: return
                buffer = createImage(size.width, size.height)
            }
        })
    }

    override fun paint(g: Graphics?) {
        val g2d = g as Graphics2D? ?: return

        if (buffer == null) {
            buffer = createImage(width, height)
        }

        val graphics = buffer!!.graphics as Graphics2D
        graphics.color = Color.BLACK
        graphics.fillRect(0, 0, width, height)

        simulationResult?.let {
            graphics.color = Color.CYAN
            synchronized(it) {
                it.state.box1.draw(graphics)
                it.state.box2.draw(graphics)

                graphics.color = Color.WHITE
                graphics.drawString("Collisions: ${it.totalCollisions}", 20, 30)
            }
        }

        g2d.drawImage(buffer, 0, 0, this)
    }

    override fun update(g: Graphics?) {
        paint(g)
    }

    private fun BoxState.draw(g: Graphics2D) {
        g.fillRect(
            ((position - width / 2.0) * scale).toInt(),
            this@PiCollisionCanvas.height - height * scale,
            width * scale,
            height * scale
        )
    }
}
