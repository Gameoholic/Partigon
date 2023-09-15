package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, will restart the animation where it was on the first frame.
 */
class RepeatLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex % duration
    }

}