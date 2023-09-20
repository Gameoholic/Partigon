package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, will continue running the animation, disregarding the loop.
 * This is the default loop used when no loop is provided.
 */
class ContinueLoop(override val duration: Int) : Loop {
    override val envelopeDuration = duration
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex
    }

}