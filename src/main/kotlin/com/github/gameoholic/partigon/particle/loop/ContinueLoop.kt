package com.github.gameoholic.partigon.particle.loop

/**
 * This loop does not affect the frame index. Nothing changes when
 * loop reaches end, the animation continues.
 *
 * @param duration The duration of the loop (used for envelope calculations). Can be 0.
 *
 * @throws IllegalArgumentException If loop duration was not above or equal to 0.
 */
class ContinueLoop(override val duration: Int) : Loop {
    override val envelopeDuration = duration
    init {
        if (duration < 0)
            throw IllegalArgumentException("Continue loop duration must be above or equal to 0.")
    }
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex
    }
}