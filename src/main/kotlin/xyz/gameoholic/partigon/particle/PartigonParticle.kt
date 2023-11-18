package xyz.gameoholic.partigon.particle

/**
 * Represents a particle animation, that's spawned every frame (the default is every tick).
 * The particle's properties are controlled via envelopes,
 * which interpolate them over time.
 */
interface PartigonParticle {
    /**
     * Starts the particle animation from frame 0.
     */
    fun start()
    /**
     * Stops the particle animation.
     */
    fun stop()
    /**
     * Resumes the particle animation from the frame it stopped.
     */
    fun resume()
}