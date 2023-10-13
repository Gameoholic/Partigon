package com.github.gameoholic.partigon.particle

import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.util.*
import com.github.gameoholic.partigon.util.Utils.envelope
import com.github.gameoholic.partigon.util.rotation.RotationOptions
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*

//todo: add link to the documentation here, also, write the documentation you lazy shit
/**
 * Represents a particle animation, that's spawned every tick.
 * The particle's properties are controlled via envelopes,
 * which interpolate them over time.
 */
class PartigonParticle(
    val originLocation: Location,
    val particleType: Particle,
    val envelopes: List<Envelope>,
    var positionX: Envelope = 0.0.envelope,
    var positionY: Envelope = 0.0.envelope,
    var positionZ: Envelope = 0.0.envelope,
    var offsetX: Envelope = 0.0.envelope,
    var offsetY: Envelope = 0.0.envelope,
    var offsetZ: Envelope = 0.0.envelope,
    val count: Envelope,
    val extra: Envelope,
    val rotationOptions: List<RotationOptions>,
    val animationFrameAmount: Int,
    val animationInterval: Int,
) {

    private constructor(
        builder: Builder
    ) :
        this(
            builder.originLocation,
            builder.particleType,
            builder.envelopes,
            builder.positionX,
            builder.positionY,
            builder.positionZ,
            builder.offsetX,
            builder.offsetY,
            builder.offsetZ,
            builder.count,
            builder.extra,
            builder.rotationOptions,
            builder.animationFrameAmount,
            builder.animationInterval,
        )

    companion object {
        inline fun partigonParticle(
            originLocation: Location,
            particleType: Particle,
            block: Builder.() -> Unit
        ) = Builder(originLocation, particleType).apply(block).build()

        inline fun partigonParticleBuilder(
            originLocation: Location,
            particleType: Particle,
            block: Builder.() -> Unit
        ) = Builder(originLocation, particleType).apply(block)
    }

    class Builder(
        var originLocation: Location,
        var particleType: Particle
    ) {
        var envelopes: List<Envelope> = listOf()
        var count: Envelope = 1.0.envelope
        var positionX: Envelope = 0.0.envelope
        var positionY: Envelope = 0.0.envelope
        var positionZ: Envelope = 0.0.envelope
        var offsetX: Envelope = 0.0.envelope
        var offsetY: Envelope = 0.0.envelope
        var offsetZ: Envelope = 0.0.envelope
        var extra: Envelope = 0.0.envelope
        var rotationOptions: List<RotationOptions> = listOf()
        var animationFrameAmount: Int = 1
        var animationInterval: Int = 1

        fun build() = PartigonParticle(this)
    }

    val id = UUID.randomUUID()!!
    var frameIndex = -1
        private set
    private var task: BukkitTask? = null
    private var delay = animationInterval

    init {
        //Add rotation for every group, on top of whatever rotations they already have
        val groups = envelopes.mapNotNull { it.envelopeGroup }.distinct()
        groups.forEach {
            it.rotationOptions = it.rotationOptions.toMutableList().apply { this.addAll(rotationOptions) }
        }
    }

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

        var newLocation = originLocation.clone().apply {
            this.x += positionX.getValueAt(frameIndex)
            this.y += positionY.getValueAt(frameIndex)
            this.z += positionZ.getValueAt(frameIndex)
        }.clone()
        var newOffset = Vector(
            offsetX.getValueAt(frameIndex),
            offsetY.getValueAt(frameIndex),
            offsetZ.getValueAt(frameIndex)
        )
        var newCount = count.getValueAt(frameIndex)
        var newExtra = extra.getValueAt(frameIndex)

        envelopes.forEach {
            val envelopePropertyType = it.propertyType
            val envelopeValue = it.getValueAt(frameIndex)
            LoggerUtil.debug("Applying envelope $it. Envelope value is $envelopeValue", id)

            when (envelopePropertyType) {
                Envelope.PropertyType.POS_X -> {
                    newLocation.x += envelopeValue
                }

                Envelope.PropertyType.POS_Y -> {
                    newLocation.y += envelopeValue
                }

                Envelope.PropertyType.POS_Z -> {
                    newLocation.z += envelopeValue
                }

                Envelope.PropertyType.OFFSET_X -> {
                    newOffset.x += envelopeValue
                }

                Envelope.PropertyType.OFFSET_Y -> {
                    newOffset.y += envelopeValue
                }

                Envelope.PropertyType.OFFSET_Z -> {
                    newOffset.z += envelopeValue
                }

                Envelope.PropertyType.COUNT -> {
                    newCount += envelopeValue.toInt()
                }

                Envelope.PropertyType.EXTRA -> {
                    newExtra += envelopeValue.toInt()
                }

                Envelope.PropertyType.NONE -> {
                    throw IllegalArgumentException("Property type NONE may only be used for nested envelopes, not for the parent one.")
                }
            }
        }

        LoggerUtil.debug("Current properties are: {location: $newLocation, count: $newCount, offset: $newOffset}", id)

        spawnParticle(newLocation, newOffset, newCount.toInt(), newExtra)
    }

    /**
     * Spawns the particle with the new provided properties.
     */
    private fun spawnParticle(newLocation: Location, newOffset: Vector, newCount: Int, newExtra: Double) {
        LoggerUtil.debug("Spawning particle", id)
        newLocation.world.spawnParticle(
            particleType,
            newLocation,
            newCount,
            newOffset.x,
            newOffset.y,
            newOffset.z,
            newExtra
        )
    }

}