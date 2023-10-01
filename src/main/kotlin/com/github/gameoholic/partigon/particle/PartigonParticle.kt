package com.github.gameoholic.partigon.particle

import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.util.LoggerUtil
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*

//todo: add link to the documentation here, also, write the documentation you lazy shit
/**
 * Represents a particle animation, that's spawned every tick.
 * The particle's properties are controlled via envelope,
 * which interpolate them over time.
 */
class PartigonParticle(
    val location: Location,
    val particleType: Particle,
    val envelopes: List<Envelope>,
    val count: Int,
    val offset: Vector,
    val animationFrameAmount: Int,
    val animationInterval: Int,
    val extra: Double,
) {

    private constructor(
        builder: Builder
    ) :
        this(
            builder.location,
            builder.particleType,
            builder.envelopes,
            builder.count,
            builder.offset,
            builder.animationFrameAmount,
            builder.animationInterval,
            builder.extra
        )

    companion object {
        inline fun partigonParticle(
            location: Location,
            particleType: Particle,
            block: Builder.() -> Unit
        ) = Builder(location, particleType).apply(block).build()
    }

    class Builder(
        val location: Location,
        val particleType: Particle
    ) {
        var envelopes: List<Envelope> = listOf()
        var count: Int = 1
        var offset: Vector = Vector(0, 0, 0)
        var animationFrameAmount: Int = 1
        var animationInterval: Int = 1
        var extra: Double = 0.0

        fun build() = PartigonParticle(this)
    }


    val id = UUID.randomUUID()!!
    var frameIndex = -1
        private set
    private var task: BukkitTask? = null
    private var delay = animationInterval

    /**
     * Resets and starts the particle animation.
     */
    fun start() {
        LoggerUtil.info("Starting PartigonParticleImpl", id)

        frameIndex = -1
        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(Partigon.plugin, 0L, 1L)
    }

    //todo: check edge casdsees for htese mthods
    /**
     * Pauses the particle animation.
     */
    fun pause() {
        LoggerUtil.info("Pausing PartigonParticleImpl", id)
        task?.cancel()
    }

    /**
     * Resumes the particle animation from the frame it stopped.
     */

    fun resume() {
        LoggerUtil.info("Resuming PartigonParticleImpl", id)
        if (task?.isCancelled == false) return
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(Partigon.plugin, 0L, 1L)
    }

    /**
     * Stops the particle animation.
     */

    fun stop() {
        LoggerUtil.info("Stopping PartigonParticleImpl", id)
        task?.cancel()
    }

    private fun onTimerTickPassed() {
        LoggerUtil.debug("Timer tick passed", id)

        //Animate animationFrameAmount frames, every animationInterval ticks
        delay += 1
        if (delay >= animationInterval) {
            for (i in 0 until animationFrameAmount) {
                frameIndex++
                applyEnvelopes()
            }
            delay = 0
        }
    }

    /**
     * Applies the envelopes and spawns the particle.
     */
    private fun applyEnvelopes() {
        LoggerUtil.debug("Applying envelopes", id)

        var newLocation = location.clone()
        var newCount = count
        var newOffset = offset.clone()

        for (envelope in envelopes.filter { !it.disabled }) {
            val envelopePropertyType = envelope.propertyType
            val envelopeValue = envelope.getValueAt(frameIndex)
            LoggerUtil.debug("Applying envelope $envelope. Envelope value is $envelopeValue", id)
            if (envelopeValue == null) {
                LoggerUtil.debug("Envelop disabled, not applying it.", id)
                continue
            }
            when (envelopePropertyType) {
                Envelope.PropertyType.POS_X -> {
                    if (envelope.isAbsolute)
                        newLocation.x = envelopeValue
                    else
                        newLocation.x += envelopeValue
                }

                Envelope.PropertyType.POS_Y -> {
                    if (envelope.isAbsolute)
                        newLocation.y = envelopeValue
                    else
                        newLocation.y += envelopeValue
                }

                Envelope.PropertyType.POS_Z -> {
                    if (envelope.isAbsolute)
                        newLocation.z = envelopeValue
                    else
                        newLocation.z += envelopeValue
                }

                Envelope.PropertyType.COUNT -> {
                    if (envelope.isAbsolute)
                        newCount = envelopeValue.toInt()
                    else
                        newCount += envelopeValue.toInt()
                }

                Envelope.PropertyType.OFFSET_X -> {
                    if (envelope.isAbsolute)
                        newOffset.x = envelopeValue
                    else
                        newOffset.x += envelopeValue
                }

                Envelope.PropertyType.OFFSET_Y -> {
                    if (envelope.isAbsolute)
                        newOffset.y = envelopeValue
                    else
                        newOffset.y += envelopeValue
                }

                Envelope.PropertyType.OFFSET_Z -> {
                    if (envelope.isAbsolute)
                        newOffset.z = envelopeValue
                    else
                        newOffset.z += envelopeValue
                }

                Envelope.PropertyType.NONE -> {
                    throw IllegalArgumentException("Property type NONE may only be used for nested envelopes, not for the parent one.")
                }
            }
        }

        LoggerUtil.debug("Current properties are: {location: $newLocation, count: $newCount, offset: $newOffset}", id)

        spawnParticle(newLocation, newCount, newOffset)
    }

    /**
     * Spawns the particle with the new provided properties.
     */
    private fun spawnParticle(newLocation: Location, newCount: Int, newOffset: Vector) {
        LoggerUtil.debug("Spawning particle", id)
        newLocation.world.spawnParticle(
            particleType,
            newLocation,
            newCount,
            newOffset.x,
            newOffset.y,
            newOffset.z,
            extra
        )
    }

}