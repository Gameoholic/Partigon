package com.github.gameoholic.partigon.partigonparticle

import com.github.gameoholic.partigon.partigonparticle.envelope.Envelope
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.Vector


interface PartigonParticle {

    val world: World
    val particleType: Particle

    val centerPosition: Vector
    val count: Int
    val offset: Vector

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