package rendering

import simulation.BoxState
import simulation.Simulation
import simulation.SimulationResult
import simulation.State
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import kotlin.concurrent.thread

class PiCollisionCanvas : Canvas() {
    private val scale = 30
    private var simulationResult = SimulationResult(
        state = State(
            time = 0.0,
            box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
            box2 = BoxState(position = 10.0, velocity = -2.0, mass = 1000000000000, width = 1, height = 1),
        ),
        totalCollisions = 0L,
    )
    private var buffer: Image? = null

    init {
        run()

        addComponentListener(object: ComponentAdapter() {
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

        graphics.color = Color.CYAN
        synchronized(simulationResult) {
            simulationResult.state.box1.draw(graphics)
            simulationResult.state.box2.draw(graphics)

            graphics.color = Color.WHITE
            graphics.drawString("Collisions: ${simulationResult.totalCollisions}", 20, 30)
        }

        g2d.drawImage(buffer, 0, 0, this)
    }

    override fun update(g: Graphics?) {
        paint(g)
    }

    private fun run() {
        Simulation.start(simulationResult.state)

        var lastTimeMillis = System.currentTimeMillis()
        thread {
            while (true) {
                Thread.sleep(1000 / 48)  // 30 FPS
                val currentTimeMillis = System.currentTimeMillis()
                synchronized(simulationResult) {
                    simulationResult = Simulation.update((currentTimeMillis - lastTimeMillis) / 1000.0)
                }
                repaint()
                lastTimeMillis = currentTimeMillis
            }
        }
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
