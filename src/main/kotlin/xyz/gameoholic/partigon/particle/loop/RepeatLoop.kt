package xyz.gameoholic.partigon.particle.loop

import org.bstats.charts.AdvancedPie
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.util.inject

/**
 * When loop reaches end, restarts the animation where it was on the first frame.
 *
 * @param duration The duration of the loop.
 *
 * @throws IllegalArgumentException If loop duration is not above 0.
 */
class RepeatLoop(override val duration: Int): Loop {
    private val plugin: PartigonPlugin by inject()

    override val envelopeDuration = duration
    init {
        if (duration <= 0)
            throw IllegalArgumentException("Repeat loop duration must be above 0.")
        plugin.metrics.addCustomChart(AdvancedPie("loopsCreated") { mapOf("Repeat" to 1) }) // bstats
    }
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex % duration
    }

}