package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, will reverse the animation, playing it the same way but from point 2 to point 1.
 * Loop duration is for both directions.
 */
class ReverseLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration / 2
    override fun applyLoop(frameIndex: Int): Int {
        //duration: 6
        //duration/2: 3
        //frameIndex: 0
        frameIndex % (duration / 2)
        return frameIndex % duration
    }

}