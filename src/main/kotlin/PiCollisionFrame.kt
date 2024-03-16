import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
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
        val canvas = PiCollisionCanvas()
        canvas.setSize(500, 400)
        canvas.setLocation(0, 50)
        add(canvas)
    }
}