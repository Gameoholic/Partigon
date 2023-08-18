package com.github.gameoholic.partigon.particleanimation

import com.github.gameoholic.partigon.particleanimation.envelope.Envelope
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.Vector


interface ParticleAnimation {

    var world: World
    var particleType: Particle

    var centerPosition: Vector
    var count: Int
    var offset: Vector

    /**
     * t = -1 when animation has yet to start, t = 0 for the first frame. for example: t >= 20 will run it for 20 frames starting from 0, stop on and NOT animate the 20th frame.
     */
    val stopCondition: (t: Int) -> Boolean

    val envelopes: List<Envelope>

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