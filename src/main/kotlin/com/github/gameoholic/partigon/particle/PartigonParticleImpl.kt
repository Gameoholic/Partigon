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

class PartigonParticleImpl(
    override val location: Location,
    override val particleType: Particle,
    override val envelopes: List<Envelope>,
    override val count: Int,
    override val offset: Vector
) : PartigonParticle {


    override val id = UUID.randomUUID()!!
    override var frameIndex = -1
    private var task: BukkitTask? = null
    override fun start() {
        LoggerUtil.info("Starting PartigonParticleImpl", id)

        frameIndex = -1
        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(Partigon.plugin, 0L, 1L)
    }

    override fun pause() {
        LoggerUtil.info("Pausing PartigonParticleImpl", id)
        task?.cancel()
    }

    override fun resume() {
        LoggerUtil.info("Resuming PartigonParticleImpl", id)
        if (task?.isCancelled == false) return
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(Partigon.plugin, 0L, 1L)
    }

    override fun stop() {
        LoggerUtil.info("Stopping PartigonParticleImpl", id)
        task?.cancel()
    }

    private fun onTimerTickPassed() {
        LoggerUtil.debug("Timer tick passed", id)
        frameIndex++

        applyEnvelopes()
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
            0.0
        )
    }

}