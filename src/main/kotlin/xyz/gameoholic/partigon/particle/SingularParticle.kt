package xyz.gameoholic.partigon.particle

import org.bstats.charts.SingleLineChart
import org.bukkit.Bukkit
import xyz.gameoholic.partigon.particle.envelope.Envelope
import xyz.gameoholic.partigon.util.*
import xyz.gameoholic.partigon.util.Utils.envelope
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.particle.envelope.EnvelopeGroup
import xyz.gameoholic.partigon.particle.location.ConstantLocation
import xyz.gameoholic.partigon.particle.location.PartigonLocation
import xyz.gameoholic.partigon.util.rotation.RotationOptions
import xyz.gameoholic.partigon.util.rotation.RotationUtil
import java.util.*

/**
 * Represents a particle animation, that's spawned every tick.
 * The particle's properties are controlled via envelopes,
 * which interpolate them over time.
 */
class SingularParticle(
    private var originLocation: PartigonLocation,
    private val particleType: Particle,
    envelopes: List<Envelope>,
    positionX: Envelope,
    positionY: Envelope,
    positionZ: Envelope,
    offsetX: Envelope,
    offsetY: Envelope,
    offsetZ: Envelope,
    count: Envelope,
    extra: Envelope,
    private val maxFrameAmount: Int?,
    private val animationFrameAmount: Int,
    private val animationInterval: Int,
    private val rotationOptions: List<RotationOptions>,
    private val envelopeGroupsRotationOptions: List<RotationOptions>
): PartigonParticle {
    private val plugin: PartigonPlugin by inject()


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
            builder.rotationOptions,
            builder.envelopeGroupsRotationOptions
        )

    companion object {
        inline fun singularParticle(
            block: Builder.() -> Unit
        ) = Builder().apply(block).build()

        inline fun singularParticleBuilder(
            block: Builder.() -> Unit
        ) = Builder().apply(block)
    }

    class Builder {
        var originLocation: PartigonLocation = ConstantLocation(Bukkit.getWorlds()[0].spawnLocation)
        var particleType = Particle.END_ROD
        var envelopes = listOf<Envelope>()
        var count: Envelope = 0.0.envelope
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
        var rotationOptions: List<RotationOptions> = listOf()
        var envelopeGroupsRotationOptions: List<RotationOptions> = listOf()

        fun build() = SingularParticle(this)

        /**
         * Adds this Envelope to the list of Envelopes of the particle.
         */
        fun Envelope.add() {
            envelopes += this
        }

        /**
         * Adds this Envelope Group to the list of Envelopes of the particle.
         */
        fun EnvelopeGroup.add() {
            envelopes += this.getEnvelopes()
        }

        /**
         * Adds this RotationOptions to the list of final Rotation Options of the particle to be applied last.
         */
        fun RotationOptions.add() {
            rotationOptions += this
        }

        /**
         * Adds this RotationOptions to all envelope groups.
         */
        fun RotationOptions.addToGroups() {
            envelopeGroupsRotationOptions += this
        }
    }

    val id = UUID.randomUUID()!!
    var frameIndex = -1
        private set
    private var task: BukkitTask? = null
    private var delay = animationInterval
    private val envelopes: List<Envelope>

    init {
        // Add rotation for every group, on top of whatever rotations they already have
        envelopes.mapNotNull { it.envelopeGroup }.distinct().forEach {
            it.rotationOptions = it.rotationOptions.toMutableList().apply { this.addAll(envelopeGroupsRotationOptions) }
        }

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

        plugin.metrics.addCustomChart(SingleLineChart("particlesCreated") { 1 }) // bstats
    }

    /**
     * Starts the particle animation from frame 0.
     */
    override fun start() {
        LoggerUtil.info("Starting PartigonParticleImpl", id)

        frameIndex = -1
        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    /**
     * Stops the particle animation.
     */

    override fun stop() {
        LoggerUtil.info("Stopping SingularParticle", id)
        task?.cancel()
    }

    /**
     * Resumes the particle animation from the frame it stopped.
     */
    override fun resume() {
        LoggerUtil.info("Resuming PartigonParticleImpl", id)
        if (task?.isCancelled == false) return
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(plugin, 0L, 1L)
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
                    newExtra += envelopeValue
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

        LoggerUtil.debug("Applying final rotations.", id)
        // Apply final rotations
        val newOffsetAfterRot = RotationUtil.applyRotationsForPoint(
            DoubleTriple(newOffset.x, newOffset.y, newOffset.z),
            rotationOptions,
            frameIndex
        )
        val newPositionAfterRot = RotationUtil.applyRotationsForPoint(
            DoubleTriple(newLocation.x, newLocation.y, newLocation.z),
            rotationOptions,
            frameIndex
        )

        newLocation.x = newPositionAfterRot.x
        newLocation.y = newPositionAfterRot.y
        newLocation.z = newPositionAfterRot.z

        newOffset.x = newOffsetAfterRot.x
        newOffset.y = newOffsetAfterRot.y
        newOffset.z = newOffsetAfterRot.z


        LoggerUtil.debug("Spawning particle $particleType at ${newLocation.world.name}, x: ${newLocation.x}, y: ${newLocation.y}, z: ${newLocation.z}, count: $newCount, extra: $newExtra, offsetx: ${newOffset.x}, offsety: ${newOffset.y}, offsetz: ${newOffset.z}", id)
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