package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, restarts the animation where it was on the first frame.
 *
 * @param duration The duration of the loop.
 */
class RepeatLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex % duration
    }

}