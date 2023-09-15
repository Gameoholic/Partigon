package com.github.gameoholic.partigon.particle.loop

interface Loop {

    /**
     * The duration the loop will last.
     */
    val duration: Int

    /**
     * The loop duration used for envelope calculations.
     */
    val envelopeDuration: Int

    /**
     * Applies the loop for a given frame index, returning a potentially different index.
     * @return The frame index to be used for the envelope, or null if envelope should be disabled.
     */
    fun applyLoop(frameIndex: Int): Int?

}