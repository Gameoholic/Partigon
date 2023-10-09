package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, restarts the animation where it was on the first frame.
 *
 * @param duration The duration of the loop.
 *
 * @throws IllegalArgumentException If loop duration was not above 0.
 */
class RepeatLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration
    init {
        if (duration <= 0)
            throw IllegalArgumentException("Repeat loop duration must be above 0.")
    }
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex % duration
    }

}