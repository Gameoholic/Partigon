package com.github.gameoholic.partigon.particle

import com.github.gameoholic.partigon.particle.envelope.Envelope
import org.bukkit.Location
import org.bukkit.Particle

interface PartigonParticle {
    val location: Location
    val particleType: Particle

    val envelopes: List<Envelope>

    var frameIndex: Int


    /**
     * Resets and starts the particle animation.
     */
    fun start()

    /**
     * Pauses the particle animation.
     */
    fun pause()

    /**
     * Resumes the particle animation from the frame it stopped.
     */
    fun resume()

    /**
     * Stops the particle animation.
     */
    fun stop()
}