package xyz.gameoholic.partigon.particle.loop

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
     * Applies the loop for a given frame index, returning a potentially different index.
     * @return The frame index to be used for the envelope.
     */
    fun applyLoop(frameIndex: Int): Int

}