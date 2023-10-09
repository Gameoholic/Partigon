package com.github.gameoholic.partigon.particle.loop

/**
 * Represents a loop for modifying frame index for envelopes.
 * Used for looping envelopes and controlling their durations.
 */
interface Loop {
    /**
     * The amount of frames the loop will last.
     */
    val duration: Int

    /**
     * The loop duration used for envelope calculations.
     */
    val envelopeDuration: Int

    /**
     * Applies the loop for a given frame index, returning a potentially different index
     * or disabling the envelope.
     * @return The frame index to be used for the envelope, or null if envelope should be disabled.
     */
    fun applyLoop(frameIndex: Int): Int?

}