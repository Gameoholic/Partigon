package xyz.gameoholic.partigon.particle.loop

import org.bstats.charts.AdvancedPie
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.util.inject

/**
 * When loop reaches end, reverses the animation, playing it the same way but from point 2 to point 1,
 * and vice versa.
 * @param duration The duration of the loop, both directions included.
 *
 * @throws IllegalArgumentException If loop duration is not above 0.
 */
class ReverseLoop(override val duration: Int): Loop {
    private val plugin: PartigonPlugin by inject()

    override val envelopeDuration = duration / 2
    init {
        if (duration <= 0)
            throw IllegalArgumentException("Reverse loop duration must be above 0.")
        plugin.metrics.addCustomChart(AdvancedPie("loopsCreated") { mapOf("Reverse" to 1) }) // bstats
    }
    override fun applyLoop(frameIndex: Int): Int {
        // For loop index 0,1,2,3,4,5 half loop index will be 0,1,2,0,1,2
        val loopIndex = frameIndex % duration
        val halfLoopIndex = loopIndex % (duration / 2)
        // If animation needs to be reversed:
        if (loopIndex >= duration / 2)
            return duration / 2 - 1 - halfLoopIndex
        return halfLoopIndex
    }

}