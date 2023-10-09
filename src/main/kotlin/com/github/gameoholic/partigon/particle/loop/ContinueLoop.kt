package com.github.gameoholic.partigon.particle.loop

/**
 * This loop does not affect the frame index. Nothing changes when
 * loop reaches end, the animation continues.
 *
 * @param duration The duration of the loop (used for envelope calculations).
 */
class ContinueLoop(override val duration: Int) : Loop {
    override val envelopeDuration = duration
    override fun applyLoop(frameIndex: Int): Int {
        return frameIndex
    }
}