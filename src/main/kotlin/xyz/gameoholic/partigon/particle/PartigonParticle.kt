package xyz.gameoholic.partigon.particle

import org.bukkit.Bukkit
import xyz.gameoholic.partigon.particle.envelope.Envelope
import xyz.gameoholic.partigon.util.*
import xyz.gameoholic.partigon.util.Utils.envelope
import xyz.gameoholic.partigon.util.rotation.RotationOptions
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import xyz.gameoholic.partigon.particle.envelope.EnvelopeGroup
import xyz.gameoholic.partigon.particle.location.ConstantLocation
import xyz.gameoholic.partigon.particle.location.PartigonLocation
import java.util.*

/**
 * Represents a particle animation, that's spawned every tick.
 * The particle's properties are controlled via envelopes,
 * which interpolate them over time.
 */
class PartigonParticle(
    var originLocation: PartigonLocation,
    val particleType: Particle = Particle.END_ROD,
    envelopes: List<Envelope> = listOf(),
    positionX: Envelope = 0.0.envelope,
    positionY: Envelope = 0.0.envelope,
    positionZ: Envelope = 0.0.envelope,
    offsetX: Envelope = 0.0.envelope,
    offsetY: Envelope = 0.0.envelope,
    offsetZ: Envelope = 0.0.envelope,
    count: Envelope = 1.0.envelope,
    extra: Envelope = 0.0.envelope,
    val maxFrameAmount: Int?,
    val animationFrameAmount: Int,
    val animationInterval: Int,
    val entity: Entity?,
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
            builder.maxFrameAmount,
            builder.animationFrameAmount,
            builder.animationInterval,
            builder.entity
        )

    companion object {
        inline fun partigonParticle(
            block: Builder.() -> Unit
        ) = Builder().apply(block).build()

        inline fun partigonParticleBuilder(
            block: Builder.() -> Unit
        ) = Builder().apply(block)
    }

    class Builder {
        var originLocation: PartigonLocation = ConstantLocation(Bukkit.getWorlds()[0].spawnLocation)
        var particleType = Particle.END_ROD
        var envelopes = listOf<Envelope>()
        var count: Envelope = 1.0.envelope
        var positionX: Envelope = 0.0.envelope
        var positionY: Envelope = 0.0.envelope
        var positionZ: Envelope = 0.0.envelope
        var offsetX: Envelope = 0.0.envelope
        var offsetY: Envelope = 0.0.envelope
        var offsetZ: Envelope = 0.0.envelope
        var extra: Envelope = 0.0.envelope
        var maxFrameAmount: Int? = null
        var animationFrameAmount: Int = 1
        var animationInterval: Int = 1
        var entity: Entity? = null

        fun build() = PartigonParticle(this)

        /**
         * Adds this Envelope to the list of Envelopes of the particle.
         */
        fun Envelope.add()
        {
            envelopes += this
        }
        fun EnvelopeGroup.add() {
            envelopes += this.getEnvelopes()
        }
    }

    val id = UUID.randomUUID()!!
    var frameIndex = -1
        private set
    private var task: BukkitTask? = null
    private var delay = animationInterval
    private val envelopes: List<Envelope>

    init {
        val newEnvelopes = envelopes.toMutableList()

        // Add all constructor-parameter envelopes to the envelopes list
        newEnvelopes += count.copyWithPropertyType(Envelope.PropertyType.COUNT)
        newEnvelopes += positionX.copyWithPropertyType(Envelope.PropertyType.POS_X)
        newEnvelopes += positionY.copyWithPropertyType(Envelope.PropertyType.POS_Y)
        newEnvelopes += positionZ.copyWithPropertyType(Envelope.PropertyType.POS_Z)
        newEnvelopes += offsetX.copyWithPropertyType(Envelope.PropertyType.OFFSET_X)
        newEnvelopes += offsetY.copyWithPropertyType(Envelope.PropertyType.OFFSET_Y)
        newEnvelopes += offsetZ.copyWithPropertyType(Envelope.PropertyType.OFFSET_Z)
        newEnvelopes += extra.copyWithPropertyType(Envelope.PropertyType.EXTRA)
        this.envelopes = newEnvelopes
    }

    /**
     * Starts the particle animation from frame 0.
     */
    fun start() {
        LoggerUtil.info("Starting PartigonParticleImpl", id)

        frameIndex = -1
        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(xyz.gameoholic.partigon.Partigon.plugin, 0L, 1L)
    }

    /**
     * Stops the particle animation.
     */

    fun stop() {
        LoggerUtil.info("Stopping PartigonParticle", id)
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
        }.runTaskTimer(xyz.gameoholic.partigon.Partigon.plugin, 0L, 1L)
    }

    private fun onTimerTickPassed() {
        LoggerUtil.debug("Timer tick passed", id)

        //Animate animationFrameAmount frames, every animationInterval ticks
        delay += 1
        if (delay >= animationInterval) {
            for (i in 0 until animationFrameAmount) {
                frameIndex++
                maxFrameAmount?.let {
                    if (frameIndex >= it) {
                        LoggerUtil.debug("Particle has passed max frame amount, stopping.")
                        stop()
                        return
                    }
                }

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

        var newLocation = originLocation.getLocation().clone()
        var newOffset = Vector(0.0, 0.0, 0.0)
        var newCount = 0
        var newExtra = 0.0

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
                    throw IllegalArgumentException("Property type NONE may not be used for top-level envelopes.")
                }
            }
        }

        LoggerUtil.debug("Current properties are: {location: $newLocation, count: $newCount, offset: $newOffset}", id)

        spawnParticle(newLocation, newOffset, newCount, newExtra)
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