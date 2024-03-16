import simulation.BoxState
import simulation.Simulation
import simulation.State
import java.awt.*
import kotlin.concurrent.thread

class PiCollisionCanvas : Canvas() {
    private val scale = 30
    private var state = State(
        time = 0.0,
        box1 = BoxState(position = 5.0, velocity = 0.0, mass = 1, width = 1, height = 1),
        box2 = BoxState(position = 10.0, velocity = -2.0, mass = 1, width = 1, height = 1),
    )
    private var buffer: Image? = null

    init {
        run()
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
        synchronized(state) {
            state.box1.draw(graphics)
            state.box2.draw(graphics)
        }

        g2d.drawImage(buffer, 0, 0, this)
    }

    override fun update(g: Graphics?) {
        paint(g)
    }

    private fun run() {
        Simulation.start(state)

        var lastTimeMillis = System.currentTimeMillis()
        thread {
            while (true) {
                Thread.sleep(1000 / 48)  // 30 FPS
                val currentTimeMillis = System.currentTimeMillis()
                synchronized(state) {
                    state = Simulation.update((currentTimeMillis - lastTimeMillis) / 1000.0).state
                }
                repaint()
                lastTimeMillis = currentTimeMillis
            }
        }
    }

    private fun BoxState.draw(g: Graphics2D) {
        g.fillRect(((position - width / 2.0) * scale).toInt(), 100 - height, width * scale, height * scale)
    }
}
