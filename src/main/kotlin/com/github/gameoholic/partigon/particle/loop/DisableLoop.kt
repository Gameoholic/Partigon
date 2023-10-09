package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, it gets disabled.
 *
 * @param duration The duration of the loop.
 *
 * @throws IllegalArgumentException If loop duration was not above 0.
 */
class DisableLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration
    init {
        if (duration <= 0)
            throw IllegalArgumentException("Disable loop duration must be above 0.")
    }
    override fun applyLoop(frameIndex: Int): Int? {
        if (frameIndex >= duration)
            return null
        return frameIndex
    }

}