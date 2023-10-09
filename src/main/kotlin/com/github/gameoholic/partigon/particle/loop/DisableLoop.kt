package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, it gets disabled.
 *
 * @param duration The duration of the loop.
 */
class DisableLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration
    override fun applyLoop(frameIndex: Int): Int? {
        if (frameIndex >= duration)
            return null
        return frameIndex
    }

}